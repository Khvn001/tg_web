package com.telegrambot.marketplace.command.user.pathway;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.enums.ProductCategoryName;
import com.telegrambot.marketplace.enums.ProductSubcategoryName;
import com.telegrambot.marketplace.enums.StateType;
import com.telegrambot.marketplace.enums.UserType;
import com.telegrambot.marketplace.repository.StateRepository;
import com.telegrambot.marketplace.service.SendMessageBuilder;
import com.telegrambot.marketplace.service.entity.BasketService;
import com.telegrambot.marketplace.service.entity.CityService;
import com.telegrambot.marketplace.service.entity.DistrictService;
import com.telegrambot.marketplace.service.entity.OrderService;
import com.telegrambot.marketplace.service.entity.ProductPortionService;
import com.telegrambot.marketplace.service.entity.ProductService;
import com.telegrambot.marketplace.config.CommandHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

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

    private static final int ONE_NUMBER = 1;
    private static final int TWO_NUMBER = 2;
    private static final int THREE_NUMBER = 3;
    private static final int FOUR_NUMBER = 4;
    private static final int FIVE_NUMBER = 5;
    private static final int SIX_NUMBER = 6;

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
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        if (UserType.ADMIN.equals(user.getPermissions())
                || UserType.COURIER.equals(user.getPermissions())
                || UserType.MODERATOR.equals(user.getPermissions())) {
            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("You do not have permission.")
                    .build();
        }

        String[] parts = update.getCommandName().split("_");
        Long districtId = Long.parseLong(parts[ONE_NUMBER]);
        District district = districtService.findById(districtId);
        Long productId = Long.parseLong(parts[TWO_NUMBER]);
        Product product = productService.findById(productId);
        ProductSubcategoryName subcategoryName = ProductSubcategoryName.valueOf(parts[THREE_NUMBER].toUpperCase());
        ProductCategoryName categoryName = ProductCategoryName.valueOf(parts[FOUR_NUMBER].toUpperCase());
        Long cityId = Long.parseLong(parts[FIVE_NUMBER]);
        CountryName countryName = CountryName.valueOf(parts[SIX_NUMBER].toUpperCase());
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
