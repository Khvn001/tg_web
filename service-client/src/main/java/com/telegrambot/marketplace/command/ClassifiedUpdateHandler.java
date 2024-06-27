package com.telegrambot.marketplace.command;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.service.entity.UserService;
import com.telegrambot.marketplace.service.handler.HandlersMap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClassifiedUpdateHandler {

    private final UserService userService;

    private final HandlersMap commandMap;

    public Answer request(final ClassifiedUpdate classifiedUpdate) {
        return commandMap.execute(classifiedUpdate,
                userService.findUserByUpdate(classifiedUpdate));
    }
}
