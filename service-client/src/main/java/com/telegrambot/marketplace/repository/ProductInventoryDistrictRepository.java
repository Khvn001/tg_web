package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInventoryDistrictRepository extends JpaRepository<ProductInventoryDistrict, Long> {
}