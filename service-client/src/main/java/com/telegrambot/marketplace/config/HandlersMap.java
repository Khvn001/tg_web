package com.telegrambot.marketplace.config;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.TelegramType;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Component
public class HandlersMap {
    private final HashMap<TelegramType, List<Handler>> hashMap = new HashMap<>();
    private final List<Handler> handlers;

    // Тут точно также находим все обработчики, просто в первом случае я использовал
    // @Autowired. Это немного лучше.
    public HandlersMap(final List<Handler> handlers) {
        this.handlers = handlers;
    }

    @PostConstruct
    private void init() {
        for (Handler handler : handlers) {
            if (!hashMap.containsKey(handler.getHandleType())) {
                hashMap.put(handler.getHandleType(), new ArrayList<>());
            }
            hashMap.get(handler.getHandleType()).add(handler);
        }

        hashMap.values().forEach(h -> h.sort(new Comparator<Handler>() {
            @Override
            public int compare(final Handler o1, final Handler o2) {
                return o2.priority() - o1.priority();
            }
        }));
    }

    public Answer execute(final ClassifiedUpdate classifiedUpdate, final User user) {
        if (!hashMap.containsKey(classifiedUpdate.getTelegramType())) {
            return new Answer();
        }

        for (Handler handler : hashMap.get(classifiedUpdate.getTelegramType())) {
            if (handler.condition(user, classifiedUpdate)) {
                return handler.getAnswer(user, classifiedUpdate);
            }
        }
        return null;
    }
}
