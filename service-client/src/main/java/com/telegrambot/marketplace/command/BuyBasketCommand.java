package com.telegrambot.marketplace.command;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.order.Basket;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.BasketService;
import com.telegrambot.marketplace.service.handler.CommandHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class BuyBasketCommand implements Command {

    private final BasketService basketService;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "TEXT";
    }

    @SneakyThrows
    @Override
    public Answer getAnswer(ClassifiedUpdate update, User user) {
        Basket basket = basketService.getBasketByUser(user);
        if (user.getBalance().compareTo(basket.getTotalSum()) >= 0) {
            user.setBalance(user.getBalance().subtract(basket.getTotalSum()));
            basketService.completePurchase(user);
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Basket purchased successfully! Thank you for your order.")
                    .buttons(getBasketOptionsButtons(user, user.getCityId(), user.getCountryName()))
                    .build();
        } else {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Insufficient balance to complete the purchase.")
                    .buttons(getBasketOptionsButtons(user, user.getCityId(), user.getCountryName()))
                    .build();
        }
    }

    private List<InlineKeyboardButton> getBasketOptionsButtons(User user, Long cityId, CountryName countryName) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton("View Basket", "/basket"));
        buttons.add(new InlineKeyboardButton("Go back to category selection", "/city_" + cityId + "_" + countryName));
        return buttons;
    }
}