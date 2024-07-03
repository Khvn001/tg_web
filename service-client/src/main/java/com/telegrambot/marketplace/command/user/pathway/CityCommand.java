package com.telegrambot.marketplace.command.user.pathway;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.inventory.ProductInventoryCity;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.repository.UserRepository;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.ProductInventoryCityService;
import com.telegrambot.marketplace.config.CommandHandler;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@AllArgsConstructor
public class CityCommand implements Command {

    private final CityService cityService;
    private final ProductInventoryCityService productInventoryCityService;
    private final UserRepository userRepository;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/city_";
    }

    @SneakyThrows
    @Override
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        if (UserType.ADMIN.equals(user.getPermissions())
                || UserType.COURIER.equals(user.getPermissions())
                || UserType.MODERATOR.equals(user.getPermissions())) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("You do not have permission.")
                    .build();
        }

        String[] parts = update.getCommandName().split("_");
        Long cityId = Long.parseLong(parts[1]);
        CountryName countryName = CountryName.valueOf(parts[2]);
        City city = cityService.findById(cityId);
        user.setCity(city);
        user.setCountry(city.getCountry());
        userRepository.save(user);

        Map<ProductCategory, List<ProductInventoryCity>> availableCategories = productInventoryCityService
                .findAvailableProductCategories(city);

        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Please select a product category:")
                .buttons(getCategoryButtons(user, availableCategories.keySet()))
                .build();
    }

    private List<InlineKeyboardButton> getCategoryButtons(final User user,
                                                          final Set<ProductCategory> categories) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton
                .builder()
                .text("Go back to countries")
                .callbackData("/country_" + user.getCountry().getName())
                .build());
        buttons.add(InlineKeyboardButton.builder()
                .text("Go back to city selection")
                .callbackData("/city_" + user.getCity().getId() + "_" + user.getCountry().getName())
                .build()
        );
        for (ProductCategory category : categories) {
            buttons.add(InlineKeyboardButton.builder()
                    .text(String.valueOf(category.getName()))
                    .callbackData("/category_" + category.getName()
                            + "_" + user.getCity().getId()
                            + "_" + user.getCountry().getName())
                    .build());
        }
        return buttons;
    }
}
