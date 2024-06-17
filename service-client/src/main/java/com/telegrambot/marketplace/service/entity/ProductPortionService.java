package com.telegrambot.marketplace.service.entity;

import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductPortionService {

    List<ProductPortion> findAvailableProducts(City city, Product product);

    Map<District, List<ProductPortion>> findAvailableDistrictsByMap(City city, Product product);

    List<ProductPortion> findAvailableByDistrictAndProductOrderByCreatedAt(District district, Product product);

    void reserveProductPortion(ProductPortion productPortion);

    void unreserveProductPortion(ProductPortion productPortion);
}
