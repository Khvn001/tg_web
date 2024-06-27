package com.telegrambot.marketplace.service.handler;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.TelegramType;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public interface Handler {
    // Какой тип сообщения будет обработан
    TelegramType getHandleType();
    // Приоритет обработчика
    int priority();
    // Условия, при которых мы воспользуемся этим обработчиком
    boolean condition(User user, ClassifiedUpdate update);
    // В этом методе, с помощью апдейта мы будем получать answer
    Answer getAnswer(User user, ClassifiedUpdate update);
}
