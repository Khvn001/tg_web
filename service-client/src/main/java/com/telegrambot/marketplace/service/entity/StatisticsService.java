package com.telegrambot.marketplace.service.entity;

import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.Country;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticsService {
    List<Object[]> getProductInventoryCityStats();

    List<Object[]> getProductInventoryDistrictStats();

    List<Country> getAvailableCountries();

    List<Country> getUnavailableCountries();

    List<City> getAvailableCities();

    List<City> getUnavailableCities();

    List<District> getAvailableDistricts();

    List<District> getUnavailableDistricts();

    List<ProductCategory> getAvailableProductCategories();

    List<ProductCategory> getUnavailableProductCategories();

    List<ProductSubcategory> getAvailableProductSubcategories();

    List<ProductSubcategory> getUnavailableProductSubcategories();

    List<Product> getAvailableProducts();

    List<Product> getUnavailableProducts();

    long getUserCount();

    BigDecimal getSumOfUserBalances();
}
