package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSubcategoryRepository extends JpaRepository<ProductSubcategory, Long> {
}