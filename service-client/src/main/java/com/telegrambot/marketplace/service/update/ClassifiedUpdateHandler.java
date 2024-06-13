package com.telegrambot.marketplace.service.update;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.service.UserService;
import com.telegrambot.marketplace.service.handler.HandlersMap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClassifiedUpdateHandler {

    private final UserService userService;

    private final HandlersMap commandMap;

    public Answer request(ClassifiedUpdate classifiedUpdate) {
        return commandMap.execute(classifiedUpdate,
                userService.findUserByUpdate(classifiedUpdate));
    }
}