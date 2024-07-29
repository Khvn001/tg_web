package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCreatedAtBefore(LocalDateTime createdAt);
    List<Order> findAllByUser(User user);
    void deleteAllByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.id = :orderId")
    void deleteByIdCustom(@Param("orderId") Long orderId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
