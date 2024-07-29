package com.telegrambot.marketplace.config.typehandlers;

import com.telegrambot.marketplace.command.Command;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.MappedSuperclass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@MappedSuperclass
@Slf4j
public abstract class AbstractHandler implements Handler {

    protected final Map<Object, Command> allCommands = new HashMap<>();
    // Найдём все команды для обработчика
    @Autowired
    private List<Command> commands;

    protected abstract HashMap<Object, Command> createMap();

    // Тут мы распихиваем команды по хэшмапе, чтобы потом было удобнее доставать :/
    @PostConstruct
    private void init() {
        commands.forEach(c -> {
            allCommands.put(c.getFindBy(), c);
            if (Objects.equals(c.handler().getName(), this.getClass().getName())) {
                createMap().put(c.getFindBy(), c);

                log.info("{} was added for {}", c.getClass().getSimpleName(), this.getClass().getSimpleName());
            }
        });
    }
}
