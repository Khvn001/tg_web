package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.Country;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.repository.OrderRepository;
import com.telegrambot.marketplace.service.entity.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order createOrder(final User user, final List<ProductPortion> productPortions) {
        if (productPortions.isEmpty()) {
            throw new IllegalArgumentException("Product portions list cannot be empty.");
        }

        Product product = productPortions.getFirst().getProduct();
        ProductSubcategory productSubcategory = productPortions.getFirst().getProductSubcategory();
        ProductCategory productCategory = productPortions.getFirst().getProductCategory();
        Country country = productPortions.getFirst().getCountry();
        City city = productPortions.getFirst().getCity();
        District district = productPortions.getFirst().getDistrict();

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setAmount(BigDecimal.valueOf(productPortions.size()));
        order.setProductSubcategory(productSubcategory);
        order.setProductCategory(productCategory);
        order.setCountry(country);
        order.setCity(city);
        order.setDistrict(district);
        order.setPrice(product.getPrice());  // Assuming there's a price field in Product
        order.setTotalSum(product.getPrice().multiply(new BigDecimal(productPortions.size())));
        order.setBasket(user.getBasket());
        order.setProductPortions(productPortions);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        log.info("Created order: {}", savedOrder);
        return savedOrder;
    }

}
