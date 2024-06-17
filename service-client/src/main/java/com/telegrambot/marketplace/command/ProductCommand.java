package com.telegrambot.marketplace.command;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.District;
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
public class ProductCommand implements Command{
    private final CityService cityService;
    private final ProductService productService;
    private final ProductPortionService productPortionService;
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
        String productId = parts[1];
        ProductSubcategoryName subcategoryName = ProductSubcategoryName.valueOf(parts[2]);
        ProductCategoryName categoryName = ProductCategoryName.valueOf(parts[3]);
        Long cityId = Long.parseLong(parts[4]);
        CountryName countryName = CountryName.valueOf(parts[5]);
        City city = cityService.findById(cityId);
        ProductSubcategory subcategory = productSubcategoryService.findByName(subcategoryName.toString());
        Product product = productService.findById(Long.valueOf(productId));

        Map<District, List<ProductPortion>> availableProducts = productPortionService.findAvailableDistrictsByMap(city, product);

        if (availableProducts.isEmpty()) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("No products available in this subcategory.")
                    .build();
        }

        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Available products in " + subcategory.getName() + " subcategory:")
                .buttons(getProductButtons(availableProducts.keySet(), product, subcategoryName, categoryName, cityId, countryName))
                .build();
    }

    private List<InlineKeyboardButton> getProductButtons(Set<District> districts, Product product, ProductSubcategoryName subcategoryName, ProductCategoryName categoryName, Long cityId, CountryName countryName) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (District district : districts) {
            buttons.add(new InlineKeyboardButton(district.getName(), "/district_" + district.getId() + "_" + product.getId() + "_" + subcategoryName + "_" + categoryName + "_" + cityId + "_" + countryName));
        }
        return buttons;
    }
}

