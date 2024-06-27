package com.telegrambot.marketplace.command;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.order.Basket;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.user.User;
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
public class OrdersCommand implements Command {

    private final BasketService basketService;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/orders";
    }

    @SneakyThrows
    @Override
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        Basket basket = basketService.getBasketByUser(user);
        buttons.add(InlineKeyboardButton.builder()
                .text("Back to Basket")
                .callbackData("/basket_menu")
                .build());
        for (Order order : basket.getOrders()) {
            buttons.add(InlineKeyboardButton.builder()
                    .text("Delete Order " + order.getId() + "_" + order.getProductSubcategory().getName()
                            + "_" + order.getProduct().getName() + "_" + order.getAmount() + "_" + order.getTotalSum())
                    .callbackData("/delete_order_" + order.getId())
                    .build());
        }
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Select an order to delete:")
                .buttons(buttons)
                .build();
    }

}
