package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.repository.ProductRepository;
import com.telegrambot.marketplace.service.entity.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product findById(final Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

}
