package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndAllowedIsTrue(Long productId);

    Product findByProductCategoryAndProductSubcategoryAndNameAndAllowedIsTrue(ProductCategory productCategory,
                                                                              ProductSubcategory productSubcategory,
                                                                              String name);

    List<Product> findByIsAllowedFalse();

    List<Product> findByIsAllowedTrue();
}
