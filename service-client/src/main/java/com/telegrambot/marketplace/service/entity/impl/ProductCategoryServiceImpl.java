package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.enums.ProductCategoryName;
import com.telegrambot.marketplace.repository.ProductCategoryRepository;
import com.telegrambot.marketplace.service.entity.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategory findByName(final String productCategoryName) {
        return productCategoryRepository.findByAllowedIsTrueAndName(ProductCategoryName.valueOf(productCategoryName))
                .orElse(null);
    }

    @Override
    public ProductCategory save(final ProductCategory category) {
        return productCategoryRepository.save(category);
    }

}
