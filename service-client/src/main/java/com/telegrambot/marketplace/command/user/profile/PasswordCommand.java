package com.telegrambot.marketplace.command.user.profile;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.location.Country;
import com.telegrambot.marketplace.entity.user.State;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.StateType;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.service.entity.CountryService;
import com.telegrambot.marketplace.service.entity.StateService;
import com.telegrambot.marketplace.service.entity.UserService;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.config.CommandHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class PasswordCommand implements Command {
    private final UserService userService;
    private final StateService stateService;
    private final CountryService countryService;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "TEXT";
    }

    @Override
    @SneakyThrows
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        User newUser = userService.findUserByUpdate(update);

        if (StateType.CREATE_PASSWORD.equals(newUser.getState().getStateType()) && newUser.getPassword() == null) {
            if (UserType.ADMIN.equals(user.getPermissions())
                    || UserType.COURIER.equals(user.getPermissions())
                    || UserType.MODERATOR.equals(user.getPermissions())) {
                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("You do not have permission.")
                        .build();
            }
            // User is setting the password
            newUser.setPassword(update.getUpdate().getMessage().getText());
            State userNewState = newUser.getState();
            userNewState.setStateType(StateType.ENTER_REFERRAL);
            stateService.save(userNewState);
            userService.save(newUser);

            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Password set successfully! If you have referral, please type referral User's hashName:")
                    .build();
        } else if (StateType.ENTER_REFERRAL.equals(newUser.getState().getStateType())) {
            if (UserType.ADMIN.equals(user.getPermissions())
                    || UserType.COURIER.equals(user.getPermissions())
                    || UserType.MODERATOR.equals(user.getPermissions())) {
                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("You do not have permission.")
                        .build();
            }
            // User is entering referral
            String referralUsername = update.getUpdate().getMessage().getText();
            if (!"no".equalsIgnoreCase(referralUsername)) {
                User referrer = userService.findByChatId(referralUsername);
                if (referrer != null) {
                    List<User> referredUsersReferrals = referrer.getReferrals();
                    referredUsersReferrals.add(newUser);
                    referrer.setReferrals(referredUsersReferrals);
                    userService.save(referrer);
                    newUser.setReferrer(referrer);
                    newUser.getState().setStateType(StateType.NONE);
                    stateService.save(newUser.getState());
                    userService.save(newUser);
                    return new SendMessageBuilder()
                            .chatId(user.getChatId())
                            .message("Referral set successfully! Please select a country:")
                            .buttons(getCountryButtons())
                            .build();
                } else {
                    return new SendMessageBuilder()
                            .chatId(user.getChatId())
                            .message("Referral username not found. Please try again or type 'no' to skip.")
                            .build();
                }

            } else {
                newUser.getState().setStateType(StateType.NONE);
                stateService.save(newUser.getState());
                userService.save(newUser);

                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("Referral skipped. Please select a country:")
                        .buttons(getCountryButtons())
                        .build();
            }
        }

        // Default response
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Command not recognized.")
                .build();
    }

    private List<InlineKeyboardButton> getCountryButtons() {
        // Fetch cities from the database with boolean field is_allowed = true
        List<Country> countries = countryService.findAllByAllowedIsTrue();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Country country : countries) {
            buttons.add(InlineKeyboardButton.builder()
                    .text(String.valueOf(country.getName()))
                    .callbackData("/country_" + country.getName())
                    .build());
        }
        return buttons;
    }

}
