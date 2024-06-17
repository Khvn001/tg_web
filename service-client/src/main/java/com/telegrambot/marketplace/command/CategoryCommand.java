package com.telegrambot.marketplace.command;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.inventory.ProductInventoryCity;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
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
public class CategoryCommand implements Command {
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
        return "/category_";
    }

    @SneakyThrows
    @Override
    public Answer getAnswer(ClassifiedUpdate update, User user) {
        String[] parts = update.getCommandName().split("_");
        ProductCategory category = productCategoryService.findByName(parts[1]);
        Long cityId = Long.parseLong(parts[2]);
        CountryName countryName = CountryName.valueOf(parts[3]);
        City city = cityService.findById(cityId);

        Map<ProductSubcategory, List<ProductInventoryCity>> availableSubcategories = productInventoryCityService
                .findAvailableProductSubcategoriesByCategory(city, category);

        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Available subcategories in " + category + " category:")
                .buttons(getProductButtons(availableSubcategories.keySet(), String.valueOf(category.getName()), cityId, countryName))
                .build();
    }

    private List<InlineKeyboardButton> getProductButtons(Set<ProductSubcategory> productSubcategories, String categoryName, Long cityId, CountryName countryName) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (ProductSubcategory productSubcategory : productSubcategories) {
            buttons.add(new InlineKeyboardButton(productSubcategory.getName(), "/subcategory_" + productSubcategory.getName() + "_" + categoryName + "_" + cityId + "_" + countryName));
        }
        return buttons;
    }
}
