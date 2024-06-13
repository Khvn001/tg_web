package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPortionRepository extends JpaRepository<ProductPortion, Long> {
}