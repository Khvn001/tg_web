package com.telegrambot.marketplace.service.command;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.service.update.ClassifiedUpdate;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public interface Command {
    // Каким обработчиком будет пользоваться команда
    Class handler();
    // С помощью чего мы найдём эту команду
    Object getFindBy();
    // Ну и тут мы уже получим ответ на самом деле
    Answer getAnswer(ClassifiedUpdate update, User user);
}
