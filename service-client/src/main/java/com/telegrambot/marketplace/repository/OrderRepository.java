package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}