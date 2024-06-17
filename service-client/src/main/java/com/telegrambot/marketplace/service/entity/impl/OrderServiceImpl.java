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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(User user, List<ProductPortion> productPortions) {
        if (productPortions.isEmpty()) {
            throw new IllegalArgumentException("Product portions list cannot be empty.");
        }

        Product product = productPortions.get(0).getProduct();
        ProductSubcategory productSubcategory = productPortions.get(0).getProductSubcategory();
        ProductCategory productCategory = productPortions.get(0).getProductCategory();
        Country country = productPortions.get(0).getCountry();
        City city = productPortions.get(0).getCity();
        District district = productPortions.get(0).getDistrict();

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

        return orderRepository.save(order);
    }


}
