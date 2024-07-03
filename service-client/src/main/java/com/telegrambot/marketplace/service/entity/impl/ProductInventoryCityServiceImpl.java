package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryCity;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import com.telegrambot.marketplace.repository.ProductInventoryCityRepository;
import com.telegrambot.marketplace.service.entity.ProductInventoryCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductInventoryCityServiceImpl implements ProductInventoryCityService {
    private final ProductInventoryCityRepository repository;

    @Override
    public List<ProductInventoryCity> findAvailableProducts(final City city) {
        return repository.findAllByCityIdAndQuantityGreaterThanEqual(city.getId(), BigDecimal.valueOf(1));
    }

    @Override
    public Map<ProductCategory, List<ProductInventoryCity>> findAvailableProductCategories(final City city) {
        List<ProductInventoryCity> availableProducts = findAvailableProducts(city);
        Map<ProductCategory, List<ProductInventoryCity>> categoryMap = availableProducts.stream()
                .filter(cityInventoryProductUnit -> cityInventoryProductUnit.getProductCategory().isAllowed())
                .filter(cityInventoryProductUnit -> cityInventoryProductUnit.getProductSubcategory().isAllowed())
                .filter(cityInventoryProductUnit -> cityInventoryProductUnit.getProduct().isAllowed())
                .collect(Collectors.groupingBy(ProductInventoryCity::getProductCategory));

        // Remove categories with less than 1 product
        categoryMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        return categoryMap;
    }

    @Override
    public Map<ProductSubcategory, List<ProductInventoryCity>> findAvailableProductSubcategoriesByCategory(
            final City city, final ProductCategory productCategory) {
        List<ProductInventoryCity> availableProducts = findAvailableProducts(city);
        Map<ProductSubcategory, List<ProductInventoryCity>> categoryMap = availableProducts.stream()
                .filter(cityInventoryProductUnit ->
                        cityInventoryProductUnit.getProductCategory().equals(productCategory))
                .filter(cityInventoryProductUnit -> cityInventoryProductUnit.getProductCategory().isAllowed())
                .filter(cityInventoryProductUnit -> cityInventoryProductUnit.getProductSubcategory().isAllowed())
                .filter(cityInventoryProductUnit -> cityInventoryProductUnit.getProduct().isAllowed())
                .collect(Collectors.groupingBy(ProductInventoryCity::getProductSubcategory));

        // Remove subcategories with less than 1 product
        categoryMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        return categoryMap;
    }

    @Override
    public Map<Product, List<ProductInventoryCity>> findAvailableProductBySubcategoryAndCategory(
            final City city, final ProductSubcategory productSubcategory, final ProductCategory productCategory) {
        List<ProductInventoryCity> availableProducts = findAvailableProducts(city);
        Map<Product, List<ProductInventoryCity>> categoryMap = availableProducts.stream()
                .filter(cityInventoryProductUnit ->
                        cityInventoryProductUnit.getProductCategory().equals(productCategory))
                .filter(cityInventoryProductUnit ->
                        cityInventoryProductUnit.getProductSubcategory().equals(productSubcategory))
                .filter(cityInventoryProductUnit -> cityInventoryProductUnit.getProductCategory().isAllowed())
                .filter(cityInventoryProductUnit -> cityInventoryProductUnit.getProductSubcategory().isAllowed())
                .filter(cityInventoryProductUnit -> cityInventoryProductUnit.getProduct().isAllowed())
                .collect(Collectors.groupingBy(ProductInventoryCity::getProduct));

        // Remove subcategories with less than 1 product
        categoryMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        return categoryMap;
    }

    @Override
    public ProductInventoryCity save(final ProductInventoryCity productInventoryCity) {
        return repository.save(productInventoryCity);
    }

    @Override
    public ProductInventoryCity findByCityAndProduct(final City city, final Product product) {
        return repository.findByCityAndProduct(city, product);
    }

}
