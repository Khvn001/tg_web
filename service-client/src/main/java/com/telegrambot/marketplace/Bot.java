package com.telegrambot.marketplace;

import com.telegrambot.marketplace.command.ClassifiedUpdateHandler;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    private final ClassifiedUpdateHandler updateHandler;

    public Bot(final @Value("${bot.token}")String botToken, final ClassifiedUpdateHandler updateHandler) {
        super(botToken);
        this.updateHandler = updateHandler;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(final @NotNull Update update) {
        log.info("Update received:");
        //Проверим, работает ли наш бот.
        log.info(update.getMessage().getText());


        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            log.info("Message text: " + messageText);

            // Convert Telegram Update to ClassifiedUpdate
            ClassifiedUpdate classifiedUpdate = new ClassifiedUpdate(update);
            log.info(classifiedUpdate.toString());

            // Process the update
            Answer answer = updateHandler.request(classifiedUpdate);
            log.info(answer.toString());

            // Send a response (assuming Answer contains a method to get the response text)
            sendMessage(answer);
        }

    }

    private void sendMessage(final Answer answer) {
        try {
            if (answer.getBotApiMethod() instanceof SendMessage message) {
                execute(message);
            } else {
                log.error("Unsupported message type: {}", answer.getBotApiMethod().getClass().getName());
            }
        } catch (TelegramApiException e) {
            log.error("Error sending message: ", e);
        }
    }


}
