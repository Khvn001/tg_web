package com.telegrambot.marketplace.service;

import com.telegrambot.marketplace.entity.user.State;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.repository.StateRepository;
import com.telegrambot.marketplace.repository.UserRepository;
import com.telegrambot.marketplace.service.update.ClassifiedUpdate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final StateRepository stateRepository;

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
            user.setChatId(classifiedUpdate.getUserId());
            user.setUserName(classifiedUpdate.getUserName());

            State state = new State();
            state.setStateValue(null);
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
}