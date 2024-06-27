package com.telegrambot.marketplace.service.entity;

import com.telegrambot.marketplace.entity.order.Basket;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.user.User;


public interface BasketService {
    Basket addOrderToBasket(User user, Order order);
    Basket getBasketByUser(User user);
    void completePurchase(User user);
    void deleteOrderFromBasket(User user, Long orderId);
}
