package com.telegrambot.marketplace.command;

import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.order.Basket;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.enums.ProductCategoryName;
import com.telegrambot.marketplace.enums.ProductSubcategoryName;
import com.telegrambot.marketplace.enums.StateType;
import com.telegrambot.marketplace.repository.StateRepository;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.BasketService;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.DistrictService;
import com.telegrambot.marketplace.service.entity.OrderService;
import com.telegrambot.marketplace.service.entity.ProductPortionService;
import com.telegrambot.marketplace.service.entity.ProductService;
import com.telegrambot.marketplace.service.handler.CommandHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DistrictCommand implements Command {
    private final ProductPortionService productPortionService;
    private final OrderService orderService;
    private final BasketService basketService;
    private final DistrictService districtService;
    private final ProductService productService;
    private final CityService cityService;
    private final StateRepository stateRepository;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "/district_";
    }

    @SneakyThrows
    @Override
    public Answer getAnswer(ClassifiedUpdate update, User user) {
        String[] parts = update.getCommandName().split("_");
        Long districtId = Long.parseLong(parts[1]);
        District district = districtService.findById(districtId);
        Long productId = Long.parseLong(parts[2]);
        Product product = productService.findById(productId);
        ProductSubcategoryName subcategoryName = ProductSubcategoryName.valueOf(parts[3]);
        ProductCategoryName categoryName = ProductCategoryName.valueOf(parts[4]);
        Long cityId = Long.parseLong(parts[5]);
        CountryName countryName = CountryName.valueOf(parts[6]);
        user.getState().setStateType(StateType.ORDER);
        user.getState().setValue(String.format("%d_%d_%s_%s_%d_%s",
                districtId, productId, subcategoryName, categoryName, cityId, countryName));
        stateRepository.save(user.getState());
        return new SendMessageBuilder()
                .chatId(user.getChatId())
                .message("You selected " + district.getName() + ". Please enter the amount you want to order:")
                .build();
    }
}