package com.telegrambot.marketplace.service.entity;

import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;

public interface ProductService {

    Product findById(Long productId);

    Product findByName(ProductCategory productCategory,
                       ProductSubcategory productSubcategory,
                       String name);

    Product save(Product product);
}
