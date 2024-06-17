package com.telegrambot.marketplace.service.entity;

import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.user.User;

public interface UserService {

    User findUserByUpdate(ClassifiedUpdate classifiedUpdate);

    User save(User user);

    User findByChatId(String chatId);
}
