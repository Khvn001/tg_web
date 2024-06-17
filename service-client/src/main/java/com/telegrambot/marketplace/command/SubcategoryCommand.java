package com.telegrambot.marketplace.command;

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
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.ProductCategoryService;
import com.telegrambot.marketplace.service.entity.ProductInventoryCityService;
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
public class SubcategoryCommand implements Command {
    private final CityService cityService;
    private final ProductInventoryCityService productInventoryCityService;
    private final ProductCategoryService productCategoryService;
    private final ProductSubcategoryService productSubcategoryService;

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
    public Answer getAnswer(ClassifiedUpdate update, User user) {
        String[] parts = update.getCommandName().split("_");
        ProductSubcategoryName subcategoryName = ProductSubcategoryName.valueOf(parts[1]);
        ProductCategoryName categoryName = ProductCategoryName.valueOf(parts[2]);
        Long cityId = Long.parseLong(parts[3]);
        CountryName countryName = CountryName.valueOf(parts[4]);
        City city = cityService.findById(cityId);
        ProductCategory category = productCategoryService.findByName(categoryName.toString());
        ProductSubcategory subcategory = productSubcategoryService.findByName(subcategoryName.toString());

        Map<Product, List<ProductInventoryCity>> availableProducts = productInventoryCityService.findAvailableProductBySubcategoryAndCategory(city, subcategory, category);

        if (availableProducts.isEmpty()) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("No products available in this subcategory.")
                    .build();
        }

        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Available products in " + subcategory.getName() + " subcategory:")
                .buttons(getProductButtons(availableProducts.keySet(), subcategoryName, categoryName, cityId, countryName))
                .build();
    }

    private List<InlineKeyboardButton> getProductButtons(Set<Product> products, ProductSubcategoryName subcategoryName, ProductCategoryName categoryName, Long cityId, CountryName countryName) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Product product : products) {
            buttons.add(new InlineKeyboardButton(product.getName(), "/product_" + product.getId() + "_" + subcategoryName + "_" + categoryName + "_" + cityId + "_" + countryName));
        }
        return buttons;
    }
}