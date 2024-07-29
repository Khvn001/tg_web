package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCreatedAtBefore(LocalDateTime createdAt);
    List<Order> findAllByUser(User user);
    void deleteAllByUser(User user);
}
