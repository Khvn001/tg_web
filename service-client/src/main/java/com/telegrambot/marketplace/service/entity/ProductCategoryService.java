package com.telegrambot.marketplace.service.entity;

import com.telegrambot.marketplace.entity.product.description.ProductCategory;

public interface ProductCategoryService {

    ProductCategory findByName(String productCategoryName) ;

}
