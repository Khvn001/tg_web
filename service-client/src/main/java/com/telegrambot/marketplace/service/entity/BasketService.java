package com.telegrambot.marketplace.service.entity;

import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.order.Basket;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.user.User;

import java.util.List;

public interface BasketService {
    Basket addOrderToBasket(User user, Order order);
}
