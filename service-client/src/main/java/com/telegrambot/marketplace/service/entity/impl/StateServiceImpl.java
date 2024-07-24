package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.user.State;
import com.telegrambot.marketplace.repository.StateRepository;
import com.telegrambot.marketplace.service.entity.StateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    @Override
    @Transactional
    public State save(final State state) {
        return stateRepository.save(state);
    }

}
