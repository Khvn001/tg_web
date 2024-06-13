package com.telegrambot.marketplace.service.command;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.service.update.ClassifiedUpdate;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.handler.CommandHandler;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/start";
    }

    @SneakyThrows
    @Override
    public Answer getAnswer(ClassifiedUpdate update, User user) {
        return new SendMessageBuilder().chatId(user.getChatId()).message("Hello!").build();
    }
}