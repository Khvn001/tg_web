package com.telegrambot.marketplace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication(exclude = {JdbcRepositoriesAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.telegrambot.marketplace.repository")
@Slf4j
public class TelegramBotApplication {

    public static void main(final String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(TelegramBotApplication.class, args);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(ctx.getBean("bot", Bot.class));
            log.info("Bot started");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
