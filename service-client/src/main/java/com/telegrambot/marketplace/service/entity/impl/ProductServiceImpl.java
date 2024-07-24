package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import com.telegrambot.marketplace.repository.ProductRepository;
import com.telegrambot.marketplace.service.entity.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product findById(final Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public Product findByName(final ProductCategory productCategory,
                              final ProductSubcategory productSubcategory,
                              final String name) {
        return productRepository.findByProductCategoryAndProductSubcategoryAndNameAndAllowedIsTrue(productCategory,
                productSubcategory, name);
    }

    @Override
    @Transactional
    public Product save(final Product product) {
        return productRepository.save(product);
    }

}
