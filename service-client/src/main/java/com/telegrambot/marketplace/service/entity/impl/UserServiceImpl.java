package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.order.Basket;
import com.telegrambot.marketplace.entity.user.State;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.StateType;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.repository.BasketRepository;
import com.telegrambot.marketplace.repository.StateRepository;
import com.telegrambot.marketplace.repository.UserRepository;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.service.entity.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final StateRepository stateRepository;
    private final BasketRepository basketRepository;

    @Override
    public User findUserByUpdate(final ClassifiedUpdate classifiedUpdate) {

        // Проверим, существует ли этот пользователь.
        if (userRepository.findByChatId(classifiedUpdate.getUserId()).isPresent()) {
            User user = userRepository.findByChatId(classifiedUpdate.getUserId()).get();

            // Если мы не смогли до этого записать имя пользователя, то запишем его.
            if (user.getUserName() == null && classifiedUpdate.getUserName() != null) {
                user.setUserName(classifiedUpdate.getUserName());
            }

            // Проверим менял ли пользователя имя.
            if (user.getUserName() != null && !user.getUserName().equals(classifiedUpdate.getUserName())) {
                user.setUserName(classifiedUpdate.getUserName());
            }

            if (!user.getName().equals(classifiedUpdate.getName())) {
                user.setName(classifiedUpdate.getName());
            }

            log.debug("User found: {}", user.getChatId());

            return user;
        }
        try {
            User user = new User();
            user.setName(classifiedUpdate.getName());
            user.setPassword("");
            user.setPermissions(UserType.BRONZE);
            user.setBalance(BigDecimal.valueOf(0));
            user.setDiscount(0L);
            user.setChatId(classifiedUpdate.getUserId());
            user.setUserName(classifiedUpdate.getUserName());
            userRepository.save(user);

            State state = new State();
            state.setStateType(StateType.CREATE_PASSWORD);
            state.setUser(user);
            stateRepository.save(state);

            Basket basket = new Basket();
            basket.setUser(user);
            basket.setTotalSum(BigDecimal.ZERO);
            basketRepository.save(basket);

            user.setState(state);
            user.setBasket(basket);
            userRepository.save(user);

            log.info("New User: {}", user.getChatId());

            return user;
        } catch (Exception e) {
            log.error("USER NOT CREATED {}", e.getMessage());
        }

        return null;
    }

    @Override
    public User save(final User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByChatId(final String chatId) {
        return userRepository.findByChatId(Long.valueOf(chatId)).orElse(null);
    }

    @Override
    public void addUserBalance(final User user, final BigDecimal amount) {
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
        log.info("User: {}. Balance has been added. Amount: {}", user.getChatId(), amount);
    }

}
