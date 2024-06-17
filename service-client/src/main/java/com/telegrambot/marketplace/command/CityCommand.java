package com.telegrambot.marketplace.command;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.inventory.ProductInventoryCity;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.ProductInventoryCityService;
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
public class CityCommand implements Command {

    private final CityService cityService;
    private final ProductInventoryCityService productInventoryCityService;

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
    public Answer getAnswer(ClassifiedUpdate update, User user) {
        String[] parts = update.getCommandName().split("_");
        Long cityId = Long.parseLong(parts[1]);
        CountryName countryName = CountryName.valueOf(parts[2]);
        City city = cityService.findById(cityId);

        Map<ProductCategory, List<ProductInventoryCity>> availableCategories = productInventoryCityService.findAvailableProductCategories(city);

        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Please select a product category:")
                .buttons(getCategoryButtons(availableCategories.keySet(), cityId, countryName))
                .build();
    }

    private List<InlineKeyboardButton> getCategoryButtons(Set<ProductCategory> categories, Long cityId, CountryName countryName) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (ProductCategory category : categories) {
            buttons.add(new InlineKeyboardButton(category.getName(), "/category_" + category.getName() + "_" + cityId + "_" + countryName));
        }
        return buttons;
    }


}
