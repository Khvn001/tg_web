package com.telegrambot.marketplace.command;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.order.Basket;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.BasketService;
import com.telegrambot.marketplace.service.handler.CommandHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class BasketCommand implements Command {

    private final BasketService basketService;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/basket_";
    }

    @Override
    public Answer getAnswer(ClassifiedUpdate update, User user) {
        String message = generateBasketContentMessage(user);
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message(message)
                .buttons(getBasketOptionsButtons(user.getCityId(), user.getCountryName()))
                .build();
    }

    private String generateBasketContentMessage(User user) {
        Basket basket = basketService.getBasketByUser(user);
        StringBuilder message = new StringBuilder("Your basket contains:\n");
        for (Order order : basket.getOrders()) {
            message.append("Order ID: ").append(order.getId()).append("\n");
        }
        return message.toString();
    }

    private List<InlineKeyboardButton> getBasketOptionsButtons(Long cityId, CountryName countryName) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton("Buy basket", "/buy_basket"));
        buttons.add(new InlineKeyboardButton("Continue shopping", "/city_" + cityId + "_" + countryName));
        return buttons;
    }
}
