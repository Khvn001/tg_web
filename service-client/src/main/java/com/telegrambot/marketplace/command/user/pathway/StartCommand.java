package com.telegrambot.marketplace.command.user.pathway;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.Country;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.enums.StateType;
import com.telegrambot.marketplace.enums.TelegramType;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.service.S3Service;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.CountryService;
import com.telegrambot.marketplace.service.entity.DistrictService;
import com.telegrambot.marketplace.service.entity.ProductCategoryService;
import com.telegrambot.marketplace.service.entity.ProductPortionService;
import com.telegrambot.marketplace.service.entity.ProductService;
import com.telegrambot.marketplace.service.entity.ProductSubcategoryService;
import com.telegrambot.marketplace.service.entity.StateService;
import com.telegrambot.marketplace.service.entity.UserService;
import com.telegrambot.marketplace.service.handler.CommandHandler;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class StartCommand implements Command {

    private final UserService userService;
    private final StateService stateService;
    private final CountryService countryService;
    private final CityService cityService;
    private final DistrictService districtService;
    private final ProductCategoryService productCategoryService;
    private final ProductSubcategoryService productSubcategoryService;
    private final ProductService productService;
    private final ProductPortionService productPortionService;
    private final S3Service s3Service;

    private static final int ARGS_SIZE = 3;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/start";
    }

    @SneakyThrows
    @Override
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        User newOrExistingUser = userService.findUserByUpdate(update);

        if (Objects.equals(newOrExistingUser.getPassword(), "")) {
            // New user, ask to set a password
            newOrExistingUser.getState().setStateType(StateType.CREATE_PASSWORD);
            stateService.save(newOrExistingUser.getState());
            userService.save(newOrExistingUser);
            return new SendMessageBuilder()
                    .chatId(newOrExistingUser.getChatId())
                    .message("Welcome" + newOrExistingUser.getUserName() + "! " +
                            "This your hashName generated by our service: " + newOrExistingUser.getChatId() +
                            ". It may be used by your friends who will use your referral." +
                            " Please set a cypher for your account by replying with text to this message. " +
                            "It is required to transfer balance from your old shop account, which is not available " +
                            "due to loss of Telegram account, to your new shop account. More information in HELP")
                    .build();
        } else if (!UserType.ADMIN.equals(user.getPermissions())
                && !UserType.MODERATOR.equals(user.getPermissions())
                && !UserType.COURIER.equals(user.getPermissions())) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Welcome back! Please select a country:")
                    .buttons(getCountryButtons())
                    .build();
        } else if (UserType.COURIER.equals(user.getPermissions())) {
            if (StateType.ADD_PRODUCT_PORTION.equals(user.getState().getStateType())) {
                return handleProductPortionForm(update, user);
            } else {
                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("Welcome courier: " + user.getChatId() +  "! Please start creating a ProductPortion:")
                        .buttons(addProductPortionButton(user))
                        .build();
            }
        } else if (UserType.MODERATOR.equals(user.getPermissions())) {
            return null;
        } else if (UserType.ADMIN.equals(user.getPermissions())) {
            return null;
        }
        return null;
    }

    private List<InlineKeyboardButton> getCountryButtons() {
        List<Country> countries = countryService.findAllByAllowedIsTrue();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Country country : countries) {
            buttons.add(InlineKeyboardButton.builder()
                    .text(String.valueOf(country.getName()))
                    .callbackData("/country_" + country.getName())
                    .build());
        }
        return buttons;
    }

    private List<InlineKeyboardButton> addProductPortionButton(final User user) {
        // Set the user state to ADD_PRODUCT_PORTION to start the form
        user.getState().setStateType(StateType.ADD_PRODUCT_PORTION);
        stateService.save(user.getState());
        userService.save(user);

        // Return the button to start the form fill process
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder()
                .text("Start ProductPortion Form")
                .callbackData("/start_productportion_form")
                .build());
        return buttons;
    }

    private Answer handleProductPortionForm(final ClassifiedUpdate update, final User user) throws Exception {
        StateType currentState = user.getState().getStateType();

        return switch (currentState) {
            case PRODUCT_PORTION_COUNTRY_CITY_DISTRICT -> handleCountryCityDistrictStep(update, user);
            case PRODUCT_PORTION_CATEGORY_SUBCATEGORY_PRODUCT -> handleCategorySubcategoryProductStep(update, user);
            case PRODUCT_PORTION_LATITUDE_LONGITUDE_AMOUNT -> handleLatitudeLongitudeAmountStep(update, user);
            case PRODUCT_PORTION_PHOTO -> handlePhotoStep(update, user);
            default -> new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Please provide the required information for the ProductPortion.")
                    .build();
        };
    }

    private Answer handleCountryCityDistrictStep(final ClassifiedUpdate update, final User user) throws Exception {
        if (update.getTelegramType() == TelegramType.TEXT) {
            String[] parts = update.getArgs().getFirst().split(" ");
            if (parts.length != ARGS_SIZE) {
                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("Please provide the country, city, and district separated by spaces.")
                        .build();
            }
            Country country = countryService.findByCountryName(CountryName.valueOf(parts[0]));
            City city = cityService.findByCountryAndName(country, parts[1]);
            District district = districtService.findByCountryAndCityAndName(country, city, parts[2]);
            if (country == null || city == null || district == null) {
                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("Invalid country, city, or district. Please try again.")
                        .build();
            }
            productPortionService.saveCountryCityDistrict(user, country, city, district);

            // Move to the next step
            user.getState().setStateType(StateType.PRODUCT_PORTION_CATEGORY_SUBCATEGORY_PRODUCT);
            stateService.save(user.getState());
            userService.save(user);

            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Please provide the product category, subcategory, and product separated by spaces.")
                    .build();
        }
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Please provide the country, city, and district separated by spaces.")
                .build();
    }

    private Answer handleCategorySubcategoryProductStep(final ClassifiedUpdate update,
                                                        final User user)
            throws Exception {
        if (update.getTelegramType() == TelegramType.TEXT) {
            String[] parts = update.getArgs().getFirst().split(" ");
            if (parts.length != ARGS_SIZE) {
                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("Please provide the product category, subcategory, and product separated by spaces.")
                        .build();
            }
            ProductCategory category = productCategoryService.findByName(parts[0]);
            ProductSubcategory subcategory = productSubcategoryService.findByName(parts[1]);
            Product product = productService.findByName(category, subcategory, parts[2]);
            if (category == null || subcategory == null || product == null) {
                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("Invalid product category, subcategory, or product. Please try again.")
                        .build();
            }
            productPortionService.saveCategorySubcategoryProduct(user, category, subcategory, product);

            // Move to the next step
            user.getState().setStateType(StateType.PRODUCT_PORTION_LATITUDE_LONGITUDE_AMOUNT);
            stateService.save(user.getState());
            userService.save(user);

            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Please provide the latitude, longitude, and amount separated by spaces.")
                    .build();
        }
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Please provide the product category, subcategory, and product separated by spaces.")
                .build();
    }

    private Answer handleLatitudeLongitudeAmountStep(final ClassifiedUpdate update, final User user) throws Exception {
        if (update.getTelegramType() == TelegramType.TEXT) {
            String[] parts = update.getArgs().getFirst().split(" ");
            if (parts.length != ARGS_SIZE) {
                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("Please provide the latitude, longitude, and amount separated by spaces.")
                        .build();
            }
            try {
                BigDecimal latitude = new BigDecimal(parts[0]);
                BigDecimal longitude = new BigDecimal(parts[1]);
                BigDecimal amount = new BigDecimal(parts[2]);
                productPortionService.saveLatitudeLongitudeAmount(user, latitude, longitude, amount);

                // Move to the next step
                user.getState().setStateType(StateType.PRODUCT_PORTION_PHOTO);
                stateService.save(user.getState());
                userService.save(user);

                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("Please upload a photo.")
                        .build();
            } catch (NumberFormatException e) {
                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("Invalid latitude, longitude, or amount. Please try again.")
                        .build();
            }
        }
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Please provide the latitude, longitude, and amount separated by spaces.")
                .build();
    }

    private Answer handlePhotoStep(final ClassifiedUpdate update, final User user) throws Exception {
        List<PhotoSize> photos = update.getUpdate().getMessage().getPhoto();
        PhotoSize largestPhoto = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);

        if (largestPhoto != null) {
            String fileId = largestPhoto.getFileId();
            String photoUrl = downloadPhotoFromTelegram(fileId, "token");
            if (photoUrl != null) {
                productPortionService.savePhoto(user, photoUrl);

                // Finish the form
                user.getState().setStateType(StateType.NONE);
                stateService.save(user.getState());
                userService.save(user);

                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("ProductPortion has been created successfully.")
                        .build();
            }
        }
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Please upload a photo.")
                .build();
    }

    private String downloadPhotoFromTelegram(final String fileId, final String botToken) {
        RestTemplate restTemplate = new RestTemplate();
        String filePathUrl = "https://api.telegram.org/bot" + botToken + "/getFile?file_id=" + fileId;
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(filePathUrl, JsonNode.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            String filePath = response.getBody().get("result").get("file_path").asText();
            String fileUrl = "https://api.telegram.org/file/bot" + botToken + "/" + filePath;

            // Download the file from the fileUrl
            byte[] fileBytes = restTemplate.getForObject(fileUrl, byte[].class);
            if (fileBytes != null) {
                // Upload to S3
                return s3Service.uploadFile("photo.jpg", fileBytes);
            }
        }
        return null;
    }

}
