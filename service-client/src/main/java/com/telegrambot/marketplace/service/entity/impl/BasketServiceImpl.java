package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.order.Basket;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.repository.BasketRepository;
import com.telegrambot.marketplace.repository.UserRepository;
import com.telegrambot.marketplace.service.entity.BasketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;

    @Override
    public Basket addOrderToBasket(User user, Order order) {
        Basket basket = user.getBasket();
        if (basket == null) {
            basket = new Basket();
            basket.setUser(user);
            basket.setTotalSum(BigDecimal.ZERO);
        }

        basket.getOrders().add(order);
        basket.setTotalSum(basket.getTotalSum().add(order.getTotalSum()));
        return basketRepository.save(basket);
    }


}