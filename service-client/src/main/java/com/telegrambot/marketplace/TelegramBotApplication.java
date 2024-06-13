package com.telegrambot.marketplace;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Apply Default Global SecurityScheme in springdoc-openapi", version = "1.0.0"),
        security = { @SecurityRequirement(name = "bearerAuth") })
public class TelegramBotApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
    }

}
