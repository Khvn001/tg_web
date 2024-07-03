package com.telegrambot.marketplace.service.entity;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryDistrict;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;

public interface ProductInventoryDistrictService {
    ProductInventoryDistrict save(ProductInventoryDistrict productInventoryDistrict);

    ProductInventoryDistrict findByDistrictAndProduct(District district, Product product);
}
