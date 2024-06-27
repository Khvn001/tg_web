package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryCity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductInventoryCityRepository extends JpaRepository<ProductInventoryCity, Long> {

    List<ProductInventoryCity> findAllByCityIdAndQuantityGreaterThanEqual(Long cityId, BigDecimal quantity);

}
