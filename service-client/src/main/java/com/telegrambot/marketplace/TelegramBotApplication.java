package com.telegrambot.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {JdbcRepositoriesAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.telegrambot.marketplace.repository")
public class TelegramBotApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
    }

}
