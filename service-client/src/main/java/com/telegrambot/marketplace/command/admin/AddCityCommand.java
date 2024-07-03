package com.telegrambot.marketplace.command.admin;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.Country;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.CountryService;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.handler.CommandHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AddCityCommand implements Command {

    private final CityService cityService;
    private final CountryService countryService;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/addcity";
    }

    @Override
    @SneakyThrows
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        if (!UserType.ADMIN.equals(user.getPermissions())) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("You do not have permission to add cities.")
                    .build();
        }

        String[] args = update.getArgs().getFirst().split(" ");
        if (args.length < 2) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Usage: /addcity <country> <city>")
                    .build();
        }

        String countryName = args[0];
        String cityName = args[1];

        Country country = countryService.findByCountryName(CountryName.valueOf(countryName));
        if (country == null) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Country not found.")
                    .build();
        }

        City city = new City();
        city.setName(cityName);
        city.setCountry(country);
        City savedCity = cityService.save(city);

        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("City " + savedCity.getName() + " added successfully to "
                        + savedCity.getCountry().getName() + ".")
                .build();
    }
}
