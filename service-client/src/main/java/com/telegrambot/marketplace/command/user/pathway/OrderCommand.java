package com.telegrambot.marketplace.command.user.pathway;

import com.telegrambot.marketplace.command.Command;
import com.telegrambot.marketplace.dto.Answer;
import com.telegrambot.marketplace.dto.ClassifiedUpdate;
import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.enums.CountryName;
import com.telegrambot.marketplace.enums.ProductCategoryName;
import com.telegrambot.marketplace.enums.ProductSubcategoryName;
import com.telegrambot.marketplace.enums.StateType;
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
public class OrderCommand implements Command {

    private final ProductPortionService productPortionService;
    private final OrderService orderService;
    private final BasketService basketService;
    private final DistrictService districtService;
    private final ProductService productService;
    private final CityService cityService;

    private static final int ZERO_NUMBER = 0;
    private static final int ONE_NUMBER = 1;
    private static final int TWO_NUMBER = 2;
    private static final int THREE_NUMBER = 3;
    private static final int FOUR_NUMBER = 4;
    private static final int FIVE_NUMBER = 5;

    @Override
    public Class handler() {
        return CommandHandler.class;
    }

    @Override
    public Object getFindBy() {
        return "TEXT";
    }

    @SneakyThrows
    @Override
    public Answer getAnswer(final ClassifiedUpdate update, final User user) {
        if (StateType.ORDER.equals(user.getState().getStateType())) {
            String[] parts = user.getState().getValue().split("_");
            Long districtId = Long.parseLong(parts[ZERO_NUMBER]);
            District district = districtService.findById(districtId);
            Long productId = Long.parseLong(parts[ONE_NUMBER]);
            Product product = productService.findById(productId);
            ProductSubcategoryName subcategoryName = ProductSubcategoryName.valueOf(parts[TWO_NUMBER]);
            ProductCategoryName categoryName = ProductCategoryName.valueOf(parts[THREE_NUMBER]);
            Long cityId = Long.parseLong(parts[FOUR_NUMBER]);
            CountryName countryName = CountryName.valueOf(parts[FIVE_NUMBER]);

            int requestedAmount = Integer.parseInt(update.getUpdate().getMessage().getText());
            // Assuming the user's message contains only the amount

            List<ProductPortion> productPortions = productPortionService
                    .findAvailableByDistrictAndProductOrderByCreatedAt(district, product);

            int availableAmount = productPortions.size();

            if (availableAmount < requestedAmount) {
                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("Requested amount is not available. " +
                                "The max available amount is " + availableAmount + ".")
                        .buttons(getMaxAmountButtons(districtId, productId, subcategoryName, categoryName,
                                cityId, countryName, BigDecimal.valueOf(availableAmount)))
                        .build();
            }

            List<ProductPortion> selectedProductPortions = new ArrayList<>();
            for (int i = 0; i < requestedAmount; i++) {
                ProductPortion productPortion = productPortions.get(i);
                selectedProductPortions.add(productPortion);
                productPortionService.reserveProductPortion(productPortion);
            }

            Order order = orderService.createOrder(user, selectedProductPortions);
            basketService.addOrderToBasket(user, order);

            return new SendMessageBuilder()
                    .chatId(user.getChatId())
                    .message("Order created successfully and added to your basket. " +
                            "You have 30 minutes to buy this order. " +
                            "Product is reserved for you for that time.")
                    .buttons(getBasketOptionsButtons(cityId, countryName))
                    .build();
        }
        return null;
    }

    private List<InlineKeyboardButton> getMaxAmountButtons(final Long districtId, final Long productId,
                                                           final ProductSubcategoryName subcategoryName,
                                                           final ProductCategoryName categoryName, final Long cityId,
                                                           final CountryName countryName,
                                                           final BigDecimal availableAmount) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder()
                .text("Add available amount to basket")
                .callbackData("/district_" + districtId + "_" + productId + "_" + subcategoryName
                        + "_" + categoryName + "_" + cityId + "_" + countryName + "_" + availableAmount)
                .build());

        buttons.add(InlineKeyboardButton.builder()
                .text("Search in other districts")
                .callbackData("/product_" + productId + "_" + subcategoryName + "_" + categoryName
                        + "_" + cityId + "_" + countryName)
                .build());

        return buttons;
    }

    private List<InlineKeyboardButton> getBasketOptionsButtons(final Long cityId, final CountryName countryName) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(InlineKeyboardButton.builder()
                .text("Show basket and purchase")
                .callbackData("/basket_")
                .build());

        buttons.add(InlineKeyboardButton.builder()
                .text("Continue shopping")
                .callbackData("/city_" + cityId + "_" + countryName)
                .build());
        return buttons;
    }
}
