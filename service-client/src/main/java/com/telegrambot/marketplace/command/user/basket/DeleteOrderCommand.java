package com.telegrambot.marketplace.command.user.basket;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
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
public class DeleteOrderCommand implements Command {

    private final BasketService basketService;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/delete_order_";
    }

    @SneakyThrows
    @Override
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        String[] parts = update.getCommandName().split("_");
        Long orderId = Long.parseLong(parts[1]);
        basketService.deleteOrderFromBasket(user, orderId);
        // Return to the menu with orders list (/orders)
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("Order deleted successfully.")
                .buttons(getOrdersList())
                .build();

    }

    private List<InlineKeyboardButton> getOrdersList() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder()
                .text("Order deleted successfully.")
                .callbackData("/orders_")
                .build());
        return buttons;
    }
}
