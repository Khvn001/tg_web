package com.telegrambot.marketplace.command.admin;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.location.Country;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.CountryService;
import com.telegrambot.marketplace.service.handler.CommandHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ToggleCountryAvailabilityCommand  implements Command {

    private final CountryService countryService;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/togglecountry";
    }

    @Override
    @SneakyThrows
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        if (!UserType.ADMIN.equals(user.getPermissions())) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("You do not have permission to toggle country availability.")
                    .build();
        }

        String[] args = update.getArgs().getFirst().split(" ");
        if (args.length < 1) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Usage: /togglecountry <city>")
                    .build();
        }

        String countryName = args[0];
        Country country = countryService.findByCountryName(CountryName.valueOf(countryName));
        if (country == null) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Country not found.")
                    .build();
        }

        country.setAllowed(!country.isAllowed());
        countryService.save(country);

        String status = country.isAllowed() ? "available" : "unavailable";
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("City " + countryName + " is now " + status + ".")
                .build();
    }
}
