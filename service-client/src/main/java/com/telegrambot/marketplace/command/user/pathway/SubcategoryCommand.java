package com.telegrambot.marketplace.command.user.pathway;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.inventory.ProductInventoryCity;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.enums.ProductCategoryName;
import com.telegrambot.marketplace.enums.ProductSubcategoryName;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.ProductCategoryService;
import com.telegrambot.marketplace.service.entity.ProductInventoryCityService;
import com.telegrambot.marketplace.service.entity.ProductSubcategoryService;
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
public class SubcategoryCommand implements Command {
    private final CityService cityService;
    private final ProductInventoryCityService productInventoryCityService;
    private final ProductCategoryService productCategoryService;
    private final ProductSubcategoryService productSubcategoryService;

    private static final int ONE_NUMBER = 1;
    private static final int TWO_NUMBER = 2;
    private static final int THREE_NUMBER = 3;
    private static final int FOUR_NUMBER = 4;

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
        if (UserType.ADMIN.equals(user.getPermissions())
                || UserType.COURIER.equals(user.getPermissions())
                || UserType.MODERATOR.equals(user.getPermissions())) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("You do not have permission.")
                    .build();
        }

        String[] parts = update.getCommandName().split("_");
        ProductSubcategoryName subcategoryName = ProductSubcategoryName.valueOf(parts[ONE_NUMBER]);
        ProductCategoryName categoryName = ProductCategoryName.valueOf(parts[TWO_NUMBER]);
        Long cityId = Long.parseLong(parts[THREE_NUMBER]);
        CountryName countryName = CountryName.valueOf(parts[FOUR_NUMBER]);
        City city = cityService.findById(cityId);
        ProductCategory category = productCategoryService.findByName(categoryName.toString());
        ProductSubcategory subcategory = productSubcategoryService.findByName(subcategoryName.toString());

        Map<Product, List<ProductInventoryCity>> availableProducts = productInventoryCityService
                .findAvailableProductBySubcategoryAndCategory(city, subcategory, category);

        if (availableProducts.isEmpty()) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("No products available in this subcategory.")
                    .build();
        }

        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Available products in " + subcategory.getName() + " subcategory:")
                .buttons(getProductButtons(availableProducts.keySet(), subcategoryName,
                        categoryName, cityId, countryName))
                .build();
    }

    private List<InlineKeyboardButton> getProductButtons(final Set<Product> products,
                                                         final ProductSubcategoryName subcategoryName,
                                                         final ProductCategoryName categoryName,
                                                         final Long cityId,
                                                         final CountryName countryName) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Product product : products) {
            buttons.add(InlineKeyboardButton.builder()
                    .text(product.getName())
                    .callbackData("/product_" + product.getId() + "_" + subcategoryName
                            + "_" + categoryName + "_" + cityId + "_" + countryName)
                    .build());
        }
        return buttons;
    }
}
