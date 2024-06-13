package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.order.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}