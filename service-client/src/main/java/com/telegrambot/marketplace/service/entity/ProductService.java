package com.telegrambot.marketplace.service.entity;

import com.telegrambot.marketplace.entity.product.description.Product;

public interface ProductService {

    Product findById(Long productId);
}
