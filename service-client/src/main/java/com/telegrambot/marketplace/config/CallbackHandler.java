package com.telegrambot.marketplace.config;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.command.user.pathway.StartCommand;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.TelegramType;
import com.telegrambot.marketplace.service.S3Service;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.CountryService;
import com.telegrambot.marketplace.service.entity.DistrictService;
import com.telegrambot.marketplace.service.entity.ProductCategoryService;
import com.telegrambot.marketplace.service.entity.ProductPortionService;
import com.telegrambot.marketplace.service.entity.ProductService;
import com.telegrambot.marketplace.service.entity.ProductSubcategoryService;
import com.telegrambot.marketplace.service.entity.StateService;
import com.telegrambot.marketplace.service.entity.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class CallbackHandler extends AbstractHandler {

    private final UserService userService;
    private final StateService stateService;
    private final CountryService countryService;
    private final CityService cityService;
    private final DistrictService districtService;
    private final ProductCategoryService productCategoryService;
    private final ProductSubcategoryService productSubcategoryService;
    private final ProductService productService;
    private final ProductPortionService productPortionService;
    private final S3Service s3Service;

    private final Map<String, Command> callbackCommands = new HashMap<>();
    private final HashMap<Object, Command> hashMap = new HashMap<>();

    @Override
    public Answer getAnswer(final User user, final ClassifiedUpdate update) {
        String callbackData = update.getCommandName();
        log.info(callbackData);
        log.info(user.getState().toString());
        log.info(callbackCommands.keySet().toString());
        log.info(callbackCommands.values().toString());

        if (callbackData != null && callbackData.contains("/start")) {
            // Handle /start command here
            return new StartCommand(userService, stateService, countryService, cityService,
                    districtService, productCategoryService, productSubcategoryService,
                    productService, productPortionService, s3Service).getAnswer(update, user);
        }

        Command callbackCommand = callbackCommands.get(callbackData);
        if (callbackCommand != null) {
            return callbackCommand.getAnswer(update, user);
        } else {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Unknown callback or you do not have permission.")
                    .build();
        }
    }

    // Methods to register callback commands
    public void registerCallbackCommand(final String name, final Command command) {
        callbackCommands.put(name, command);
    }

    @Override
    protected HashMap<Object, Command> createMap() {
        log.info(String.valueOf(hashMap));
        return hashMap;
    }

    @Override
    public TelegramType getHandleType() {
        return TelegramType.CALL_BACK;
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public boolean condition(final User user, final ClassifiedUpdate update) {
        return TelegramType.CALL_BACK.equals(update.getTelegramType());
    }
}
