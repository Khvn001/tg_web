package com.telegrambot.marketplace.service.scheduled;

import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.order.Basket;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.repository.BasketRepository;
import com.telegrambot.marketplace.repository.OrderRepository;
import com.telegrambot.marketplace.service.entity.ProductPortionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ExpiredOrderScheduler {
    private final OrderRepository orderRepository;
    private final ProductPortionService productPortionService;

    private static final int MINUTE = 60000;
    private static final int INTERVAL_MINUTES = 30;
    private final BasketRepository basketRepository;

    @Scheduled(fixedRate = MINUTE)  // Run every minute
    @Transactional
    public void deleteExpiredOrders() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(INTERVAL_MINUTES);
        List<Order> expiredOrders = orderRepository.findByCreatedAtBefore(expirationTime);

        for (Order order : expiredOrders) {
            try {
                for (ProductPortion productPortion : order.getProductPortions()) {
                    productPortionService.unreserveProductPortion(productPortion);
                }
                log.info("Starting deletion of expired order {}", order);
                Basket basket = order.getBasket();
                basket.getOrders().remove(order);
                basket.setTotalSum(basket.getTotalSum().subtract(order.getTotalSum()));
                Basket savedBasket = basketRepository.save(basket);
                log.info(savedBasket.toString());
                order.setBasket(null);
                order.setProductPortions(null);
                orderRepository.save(order);
                orderRepository.delete(order);
                log.info("Deleted expired order with ID: {}", order.getId());
            } catch (Exception e) {
                log.error("Failed to delete expired order with ID: {}", order.getId(), e);
            }
        }
        log.info("Expired orders have been deleted");
    }
}
