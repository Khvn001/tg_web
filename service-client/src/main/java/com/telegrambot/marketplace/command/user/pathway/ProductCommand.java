package com.telegrambot.marketplace.command.user.pathway;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.enums.ProductCategoryName;
import com.telegrambot.marketplace.enums.ProductSubcategoryName;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.ProductCategoryService;
import com.telegrambot.marketplace.service.entity.ProductPortionService;
import com.telegrambot.marketplace.service.entity.ProductService;
import com.telegrambot.marketplace.service.entity.ProductSubcategoryService;
import com.telegrambot.marketplace.service.handler.CommandHandler;
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
public class ProductCommand implements Command {
    private final CityService cityService;
    private final ProductService productService;
    private final ProductPortionService productPortionService;
    private final ProductCategoryService productCategoryService;
    private final ProductSubcategoryService productSubcategoryService;

    private static final int ONE_NUMBER = 1;
    private static final int TWO_NUMBER = 2;
    private static final int THREE_NUMBER = 3;
    private static final int FOUR_NUMBER = 4;
    private static final int FIVE_NUMBER = 5;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/subcategory_";
    }

    @SneakyThrows
    @Override
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        String[] parts = update.getCommandName().split("_");
        String productId = parts[ONE_NUMBER];
        ProductSubcategoryName subcategoryName = ProductSubcategoryName.valueOf(parts[TWO_NUMBER]);
        ProductCategoryName categoryName = ProductCategoryName.valueOf(parts[THREE_NUMBER]);
        Long cityId = Long.parseLong(parts[FOUR_NUMBER]);
        CountryName countryName = CountryName.valueOf(parts[FIVE_NUMBER]);
        City city = cityService.findById(cityId);
        ProductSubcategory subcategory = productSubcategoryService.findByName(subcategoryName.toString());
        Product product = productService.findById(Long.valueOf(productId));

        Map<District, List<ProductPortion>> availableProducts = productPortionService
                .findAvailableDistrictsByMap(city, product);

        if (availableProducts.isEmpty()) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("No products available in this subcategory.")
                    .build();
        }

        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Available products in " + subcategory.getName() + " subcategory:")
                .buttons(getProductButtons(availableProducts.keySet(), product, subcategoryName,
                        categoryName, cityId, countryName))
                .build();
    }

    private List<InlineKeyboardButton> getProductButtons(final Set<District> districts,
                                                         final Product product,
                                                         final ProductSubcategoryName subcategoryName,
                                                         final ProductCategoryName categoryName,
                                                         final Long cityId,
                                                         final CountryName countryName) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (District district : districts) {
            buttons.add(InlineKeyboardButton.builder()
                    .text(district.getName())
                    .callbackData("/district_" + district.getId() + "_" + product.getId() + "_" + subcategoryName
                            + "_" + categoryName + "_" + cityId + "_" + countryName)
                    .build());        }
        return buttons;
    }
}
