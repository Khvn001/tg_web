package com.telegrambot.marketplace.service.handler;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.TelegramType;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.command.Command;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CommandHandler extends AbstractHandler {

    private HashMap<Object, Command> hashMap = new HashMap<>();

    @Override
    protected HashMap<Object, Command> createMap() {
        return hashMap;
    }

    @Override
    public TelegramType getHandleType() {
        return TelegramType.COMMAND;
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public boolean condition(User user, ClassifiedUpdate update) {
        return hashMap.containsKey(update.getCommandName());
    }

    @Override
    public Answer getAnswer(User user, ClassifiedUpdate update) {
        return hashMap.get(update.getCommandName()).getAnswer(update, user);
    }
}