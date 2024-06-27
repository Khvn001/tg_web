package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.order.Basket;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.repository.BasketRepository;
import com.telegrambot.marketplace.service.entity.BasketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;

    @Override
    public Basket addOrderToBasket(final User user, final Order order) {
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

    @Override
    public Basket getBasketByUser(final User user) {
        return basketRepository.findById(user.getBasket().getId()).orElse(null);
    }

    @Override
    public void completePurchase(final User user) {
        Basket basket = user.getBasket();
        basket.getOrders().clear();
        basket.setTotalSum(BigDecimal.ZERO);
        basketRepository.save(basket);
    }

    @Override
    public void deleteOrderFromBasket(final User user, final Long orderId) {
        Basket basket = user.getBasket();
        Order orderToRemove = basket.getOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElse(null);
        if (orderToRemove != null) {
            basket.getOrders().remove(orderToRemove);
            basket.setTotalSum(basket.getTotalSum().subtract(orderToRemove.getTotalSum()));
            basketRepository.save(basket);
        }
    }

}
