package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryCity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInventoryCityRepository extends JpaRepository<ProductInventoryCity, Long> {
}