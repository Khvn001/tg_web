package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.user.UserSubcategoryDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubcategoryDiscountRepository extends JpaRepository<UserSubcategoryDiscount, Long> {
}