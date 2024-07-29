package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.order.Basket;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.user.User;
import com.telegrambot.marketplace.repository.BasketRepository;
import com.telegrambot.marketplace.repository.OrderRepository;
import com.telegrambot.marketplace.service.entity.BasketService;
import com.telegrambot.marketplace.service.entity.ProductPortionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;
    private final ProductPortionService productPortionService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Basket addOrderToBasket(final User user, final Order order) {
        Basket basket = user.getBasket();
        if (basket == null) {
            basket = new Basket();
            basket.setUser(user);
            basket.setTotalSum(BigDecimal.ZERO);
        }

        basket.getOrders().add(order);
        basket.setTotalSum(basket.getTotalSum().add(order.getTotalSum()));
        log.debug("User: {}. Order {} was added to basket", user.getChatId(), order.getId());
        return basketRepository.save(basket);
    }

    @Override
    public Basket getBasketByUser(final User user) {
        return basketRepository.findById(user.getBasket().getId()).orElse(null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void completePurchase(final User user) {
        Basket basket = user.getBasket();
        basket.getOrders().clear();
        orderRepository.deleteAllByUser(user);
        basket.setTotalSum(BigDecimal.ZERO);
        basketRepository.save(basket);
        log.info("User: {}. Basket was fully purchased", user.getChatId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteOrderFromBasket(final User user, final Long orderId) {
        Basket basket = user.getBasket();
        Order orderToRemove = basket.getOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElse(null);
        if (orderToRemove != null) {
            log.info(orderToRemove.toString());
            List<ProductPortion> productPortions = orderToRemove.getProductPortions();
            for (ProductPortion productPortion : productPortions) {
                productPortionService.unreserveProductPortion(productPortion);
                log.info(productPortion.toString());
            }
            basket.getOrders().remove(orderToRemove);
            basket.setTotalSum(basket.getTotalSum().subtract(orderToRemove.getTotalSum()));
            Basket savedBasket = basketRepository.save(basket);
            log.info(savedBasket.toString());
            log.info(orderToRemove.toString());
            orderToRemove.getProductPortions().forEach(pp -> pp.setOrder(null));
            orderToRemove.setBasket(null);
            orderToRemove.setProductPortions(new ArrayList<>());
            orderToRemove.setCity(null);
            orderToRemove.setCountry(null);
            orderToRemove.setDistrict(null);
            orderToRemove.setProduct(null);
            orderToRemove.setProductCategory(null);
            orderToRemove.setProductSubcategory(null);
            orderRepository.save(orderToRemove);
            orderRepository.deleteByIdCustom(orderId);
        }
        log.info("User: {}. Order: {} was deleted", user.getChatId(), orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAllOrdersFromBasket(final User user) {
        Basket basket = user.getBasket();
        basket.setOrders(new ArrayList<>());
        basket.setTotalSum(BigDecimal.ZERO);
        basketRepository.save(basket);
        List<Order> orders = orderRepository.findAllByUser(user);
        for (Order order : orders) {
            List<ProductPortion> productPortions = order.getProductPortions();
            for (ProductPortion productPortion : productPortions) {
                productPortionService.unreserveProductPortion(productPortion);
            }
        }
        orderRepository.deleteAllByUser(user);
        log.info("User: {}. All Orders were deleted", user.getChatId());
    }

}
