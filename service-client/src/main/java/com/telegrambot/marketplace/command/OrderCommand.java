package com.telegrambot.marketplace.command;

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
    public Answer getAnswer(ClassifiedUpdate update, User user) {
        if(StateType.ORDER.equals(user.getState().getStateType())) {
            String[] parts = user.getState().getValue().split("_");
            Long districtId = Long.parseLong(parts[0]);
            District district = districtService.findById(districtId);
            Long productId = Long.parseLong(parts[1]);
            Product product = productService.findById(productId);
            ProductSubcategoryName subcategoryName = ProductSubcategoryName.valueOf(parts[2]);
            ProductCategoryName categoryName = ProductCategoryName.valueOf(parts[3]);
            Long cityId = Long.parseLong(parts[4]);
            CountryName countryName = CountryName.valueOf(parts[5]);

            int requestedAmount = Integer.parseInt(update.getUpdate().getMessage().getText()); // Assuming the user's message contains only the amount

            List<ProductPortion> productPortions = productPortionService.findAvailableByDistrictAndProductOrderByCreatedAt(district, product);

            int availableAmount = productPortions.size();

            if (availableAmount < requestedAmount) {
                return new SendMessageBuilder()
                        .chatId(user.getChatId())
                        .message("Requested amount is not available. The max available amount is " + availableAmount + ".")
                        .buttons(getMaxAmountButtons(districtId, productId, subcategoryName, categoryName, cityId, countryName, BigDecimal.valueOf(availableAmount)))
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
                    .message("Order created successfully and added to your basket. You have 30 minutes to buy this order. " +
                            "Product is reserved for you for that time.")
                    .buttons(getBasketOptionsButtons(cityId, countryName))
                    .build();
        }
        return null;
    }

    private List<InlineKeyboardButton> getMaxAmountButtons(Long districtId, Long productId, ProductSubcategoryName subcategoryName, ProductCategoryName categoryName, Long cityId, CountryName countryName, BigDecimal availableAmount) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton("Add available amount to basket", "/district_" + districtId + "_" + productId + "_" + subcategoryName + "_" + categoryName + "_" + cityId + "_" + countryName + "_" + availableAmount));
        buttons.add(new InlineKeyboardButton("Search in other districts", "/product_" + productId + "_" + subcategoryName + "_" + categoryName + "_" + cityId + "_" + countryName));
        return buttons;
    }

    private List<InlineKeyboardButton> getBasketOptionsButtons(Long cityId, CountryName countryName) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton("Show basket and purchase", "/basket"));
        buttons.add(new InlineKeyboardButton("Continue shopping", "/city_" + cityId + "_" + countryName));
        return buttons;
    }
}
