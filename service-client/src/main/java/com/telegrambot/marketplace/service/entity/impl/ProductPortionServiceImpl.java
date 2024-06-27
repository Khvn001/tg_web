package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.repository.ProductPortionRepository;
import com.telegrambot.marketplace.service.entity.ProductPortionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductPortionServiceImpl implements ProductPortionService {

    private final ProductPortionRepository productPortionRepository;

    @Override
    public List<ProductPortion> findAvailableProducts(final City city, final Product product) {
        return productPortionRepository.findAllByCityAndProduct(city, product);
    }

    @Override
    public Map<District, List<ProductPortion>> findAvailableDistrictsByMap(final City city, final Product product) {
        List<ProductPortion> availableProducts = findAvailableProducts(city, product);
        Map<District, List<ProductPortion>> categoryMap = availableProducts.stream()
                .filter(productPortion -> productPortion.getDistrict().isAllowed())
                .collect(Collectors.groupingBy(ProductPortion::getDistrict));
        categoryMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        return categoryMap;
    }

    public List<ProductPortion> findAllByDistrictAndProduct(final District district, final Product product) {
        return productPortionRepository.findAllByDistrictAndProduct(district, product);
    }

    public void updateAmount(final ProductPortion productPortion, final BigDecimal amount) {
        productPortion.setAmount(productPortion.getAmount().add(amount));
        productPortionRepository.save(productPortion);
    }

    public List<ProductPortion> findAvailableByDistrictAndProductOrderByCreatedAt(final District district,
                                                                                  final Product product) {
        return productPortionRepository.findByDistrictAndProductAndReservedFalseOrderByCreatedAtAsc(district, product);
    }

    public void reserveProductPortion(final ProductPortion productPortion) {
        productPortion.setReserved(true);
        productPortionRepository.save(productPortion);
    }

    public void unreserveProductPortion(final ProductPortion productPortion) {
        productPortion.setReserved(false);
        productPortionRepository.save(productPortion);
    }

}
