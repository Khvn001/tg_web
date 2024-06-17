package com.telegrambot.marketplace.service.entity;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryCity;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;

import java.util.List;
import java.util.Map;

public interface ProductInventoryCityService {

    List<ProductInventoryCity> findAvailableProducts(City city);

    Map<ProductCategory, List<ProductInventoryCity>> findAvailableProductCategories(City city);

    Map<ProductSubcategory, List<ProductInventoryCity>> findAvailableProductSubcategoriesByCategory(City city, ProductCategory productCategory);

    Map<Product, List<ProductInventoryCity>> findAvailableProductBySubcategoryAndCategory(City city, ProductSubcategory productSubcategory, ProductCategory productCategory);

}
