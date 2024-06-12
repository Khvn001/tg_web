package com.sellplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TelegramBotApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
    }

    @Bean
    public TelegramBotApplication bot() {
        TelegramBotApplication bot = new TelegramBotApplication();
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot((LongPollingBot) bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return bot;
    }
}
