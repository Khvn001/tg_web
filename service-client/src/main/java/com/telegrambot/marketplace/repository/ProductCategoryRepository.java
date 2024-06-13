package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}