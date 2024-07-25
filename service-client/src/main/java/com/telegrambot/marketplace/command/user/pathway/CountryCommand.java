package com.telegrambot.marketplace.command.user.pathway;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.Country;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.repository.UserRepository;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.CountryService;
import com.telegrambot.marketplace.service.entity.ProductInventoryCityService;
import com.telegrambot.marketplace.config.CommandHandler;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CountryCommand implements Command {

    private final CountryService countryService;
    private final CityService cityService;
    private final ProductInventoryCityService productInventoryCityService;
    private final UserRepository userRepository;

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
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        if (UserType.ADMIN.equals(user.getPermissions())
                || UserType.COURIER.equals(user.getPermissions())
                || UserType.MODERATOR.equals(user.getPermissions())) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("You do not have permission.")
                    .build();
        }

        CountryName countryName = CountryName.valueOf(update.getCommandName().split("_")[1].toUpperCase());
        log.info(countryName.getCountry());
        user.setCountry(countryService.findByCountryName(countryName));
        userRepository.save(user);
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Please select a city or go back:")
                .buttons(getCityButtons(countryName))
                .build();
    }

    private List<InlineKeyboardButton> getCityButtons(final CountryName countryName) {
        // Fetch cities from the database with boolean field is_allowed = true
        Country country = countryService.findByCountryName(countryName);
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder()
                .text("Change Country")
                .callbackData("/start")
                .build());
        if (country != null) {
            List<City> cities = cityService.findByCountryIdAndAllowed(country.getId());
            log.info("{} cities found", cities.size());
            for (City city : cities) {
                if (productInventoryCityService.findAvailableProducts(city) == null) {
                    continue;
                }
                buttons.add(InlineKeyboardButton.builder()
                        .text(city.getName())
                        .callbackData("/city_" + city.getId() + "_" + countryName)
                        .build());
            }
        }
        return buttons;
    }
}
