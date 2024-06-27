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
import lombok.SneakyThrows;
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

    @SneakyThrows
    @Override
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        String message = generateBasketContentMessage(user);
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message(message)
                .buttons(getBasketOptionsButtons(user.getCity().getId(), user.getCountry().getName()))
                .build();
    }

    private String generateBasketContentMessage(final User user) {
        Basket basket = basketService.getBasketByUser(user);
        StringBuilder message = new StringBuilder("Your basket contains:\n");
        for (Order order : basket.getOrders()) {
            message.append("Order ID: ").append(order.getId()).append("\n");
        }
        return message.toString();
    }

    private List<InlineKeyboardButton> getBasketOptionsButtons(final Long cityId, final CountryName countryName) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder()
                .text("Spend balance to buy basket")
                .callbackData("/buyBasket")
                .build());

        buttons.add(InlineKeyboardButton.builder()
                .text("Monitor orders")
                .callbackData("/orders")
                .build());

        buttons.add(InlineKeyboardButton.builder()
                .text("Go back to category selection")
                .callbackData("/city_" + cityId + "_" + countryName)
                .build());

        return buttons;
    }

}
