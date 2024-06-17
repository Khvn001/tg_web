package com.telegrambot.marketplace.command;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.Country;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.CountryService;
import com.telegrambot.marketplace.service.entity.ProductInventoryCityService;
import com.telegrambot.marketplace.service.handler.CommandHandler;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CountryCommand implements Command {

    private final CountryService countryService;
    private final CityService cityService;
    private final ProductInventoryCityService productInventoryCityService;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/country_";
    }

    @SneakyThrows
    @Override
    public Answer getAnswer(ClassifiedUpdate update, User user) {
        CountryName countryName = CountryName.valueOf(update.getCommandName().split("_")[1]);
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Please select a city:")
                .buttons(getCityButtons(countryName))
                .build();
    }

    private List<InlineKeyboardButton> getCityButtons(CountryName countryName) {
        // Fetch cities from the database with boolean field is_allowed = true
        Country country = countryService.findByCountryName(countryName);
        List<City> cities = cityService.findByCountryIdAndAllowed(country.getId());
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (City city : cities) {
            if (productInventoryCityService.findAvailableProducts(city) == null) {
                continue;
            }
            buttons.add(new InlineKeyboardButton(city.getName(), "/city_" + city.getId() + "_" + countryName));
        }
        return buttons;
    }
}