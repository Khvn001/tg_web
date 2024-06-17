package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.user.State;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.StateType;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.repository.StateRepository;
import com.telegrambot.marketplace.repository.UserRepository;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.service.entity.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final StateRepository stateRepository;

    @Override
    public User findUserByUpdate(ClassifiedUpdate classifiedUpdate) {

        // Проверим, существует ли этот пользователь.
        if(userRepository.findByChatId(classifiedUpdate.getUserId()).isPresent()) {
            User user = userRepository.findByChatId(classifiedUpdate.getUserId()).get();

            // Если мы не смогли до этого записать имя пользователя, то запишем его.
            if(user.getUserName() == null && classifiedUpdate.getUserName() != null)
                user.setUserName(classifiedUpdate.getUserName());

            // Проверим менял ли пользователя имя.
            if(user.getUserName() != null)
                if (!user.getUserName().equals(classifiedUpdate.getUserName()))
                    user.setUserName(classifiedUpdate.getUserName());

            if(!user.getName().equals(classifiedUpdate.getName()))
                user.setName(classifiedUpdate.getName());

            return user;
        }
        try {
            User user = new User();
            user.setName(classifiedUpdate.getName());
            user.setPermissions(UserType.DEFAULT);
            user.setBalance(BigDecimal.valueOf(0));
            user.setDiscount(0L);
            user.setChatId(classifiedUpdate.getUserId());
            user.setUserName(classifiedUpdate.getUserName());

            State state = new State();
            state.setStateType(StateType.CREATE_PASSWORD);
            state.setUser(user);

            stateRepository.save(state);

            user.setState(state);
            userRepository.save(user);

            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByChatId(String chatId) {
        return userRepository.findByChatId(chatId).orElse(null);
    }
}