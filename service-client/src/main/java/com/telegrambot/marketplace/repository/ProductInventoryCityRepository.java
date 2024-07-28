package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryCity;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.product.description.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductInventoryCityRepository extends JpaRepository<ProductInventoryCity, Long> {

    List<ProductInventoryCity> findAllByCityIdAndQuantityGreaterThanEqual(Long cityId, BigDecimal quantity);

    @Query("SELECT pic.product, pic.city, SUM(pic.quantity) " +
            "FROM ProductInventoryCity pic " +
            "GROUP BY pic.product, pic.city")
    List<Object[]> findGroupedByCityAndProduct();

    Optional<ProductInventoryCity> findByCityAndProduct(City city, Product product);
}
