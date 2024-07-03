package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryDistrict;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductInventoryDistrictRepository extends JpaRepository<ProductInventoryDistrict, Long> {

    @Query("SELECT pid.product, pid.district, SUM(pid.quantity) " +
            "FROM ProductInventoryDistrict pid " +
            "GROUP BY pid.product, pid.district")
    List<Object[]> findGroupedByDistrictAndProduct();

    ProductInventoryDistrict findByDistrictAndProduct(District district, Product product);
}
