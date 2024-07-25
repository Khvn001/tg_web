package com.telegrambot.marketplace.config;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.TelegramType;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CallbackHandler extends AbstractHandler {

    private final Map<String, Command> callbackCommands = new HashMap<>();
    private final HashMap<Object, Command> hashMap = new HashMap<>();

    @Override
    public Answer getAnswer(final User user, final ClassifiedUpdate update) {
        String callbackData = update.getCommandName();
        log.info(callbackData);
        log.info(user.getState().toString());
        log.info(callbackCommands.keySet().toString());
        log.info(callbackCommands.values().toString());

        Command callbackCommand = callbackCommands.get(callbackData);
        if (callbackCommand != null) {
            return callbackCommand.getAnswer(update, user);
        } else {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Unknown callback or you do not have permission.")
                    .build();
        }
    }

    // Methods to register callback commands
    public void registerCallbackCommand(final String name, final Command command) {
        callbackCommands.put(name, command);
    }

    @Override
    protected HashMap<Object, Command> createMap() {
        log.info(String.valueOf(hashMap));
        return hashMap;
    }

    @Override
    public TelegramType getHandleType() {
        return TelegramType.CALL_BACK;
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public boolean condition(final User user, final ClassifiedUpdate update) {
        return TelegramType.CALL_BACK.equals(update.getTelegramType());
    }
}
