package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryDistrict;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.repository.ProductInventoryDistrictRepository;
import com.telegrambot.marketplace.service.entity.ProductInventoryDistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductInventoryDistrictServiceImpl implements ProductInventoryDistrictService {

    private final ProductInventoryDistrictRepository repository;

    @Override
    public ProductInventoryDistrict save(final ProductInventoryDistrict productInventoryDistrict) {
        return repository.save(productInventoryDistrict);
    }

    @Override
    public ProductInventoryDistrict findByDistrictAndProduct(final District district, final Product product) {
        return repository.findByDistrictAndProduct(district, product);
    }
}
