package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPortionRepository extends JpaRepository<ProductPortion, Long> {

    List<ProductPortion> findAllByCityAndProduct(City city, Product product);

    List<ProductPortion> findAllByDistrictAndProduct(District district, Product product);

    List<ProductPortion> findByDistrictAndProductAndReservedFalseOrderByCreatedAtAsc(
            District district, Product product);

}
