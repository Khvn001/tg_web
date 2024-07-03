package com.telegrambot.marketplace.command.admin;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.handler.CommandHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ToggleCityAvailabilityCommand implements Command {

    private final CityService cityService;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/togglecity";
    }

    @Override
    @SneakyThrows
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        if (!UserType.ADMIN.equals(user.getPermissions())) {
            return new SendMessageBuilder().chatId(user.getChatId())
                    .message("You do not have permission to toggle city availability.")
                    .build();
        }

        String[] args = update.getArgs().getFirst().split(" ");
        if (args.length < 1) {
            return new SendMessageBuilder().chatId(user.getChatId()).message("Usage: /togglecity <city>").build();
        }

        String cityName = args[0];
        City city = cityService.findByName(cityName);
        if (city == null) {
            return new SendMessageBuilder().chatId(user.getChatId()).message("City not found.").build();
        }

        city.setAllowed(!city.isAllowed());
        cityService.save(city);

        String status = city.isAllowed() ? "available" : "unavailable";
        return new SendMessageBuilder()
                .chatId(user.getChatId()).message("City " + cityName + " is now " + status + ".")
                .build();
    }
}
