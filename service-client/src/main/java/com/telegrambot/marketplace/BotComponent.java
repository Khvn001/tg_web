package com.telegrambot.marketplace;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class BotComponent extends TelegramLongPollingBot {

    // Создаём их объект для регистрации
    private final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    @PostConstruct
    private void init() {
        try {
            telegramBotsApi.registerBot(this); // Регистрируем бота
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public BotComponent() throws TelegramApiException {
    }

    @Override
    public void onUpdateReceived(final @NotNull Update update) {
        //Проверим, работает ли наш бот.
        log.info(update.getMessage().getText());
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

}
