package com.telegrambot.marketplace;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    public Bot(final @Value("${bot.token}")String botToken) {
        super(botToken);
    }

    @Value("${bot.name}")
    private String botName;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(final @NotNull Update update) {
        //Проверим, работает ли наш бот.
        log.info(update.getMessage().getText());
    }

}
