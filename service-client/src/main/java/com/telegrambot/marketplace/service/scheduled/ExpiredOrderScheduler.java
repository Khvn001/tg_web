package com.telegrambot.marketplace.service.scheduled;

import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.repository.OrderRepository;
import com.telegrambot.marketplace.service.entity.ProductPortionService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ExpiredOrderScheduler {
    private final OrderRepository orderRepository;
    private final ProductPortionService productPortionService;

    private static final int MINUTE = 60000;
    private static final int INTERVAL_MINUTES = 1;

    @Scheduled(fixedRate = MINUTE)  // Run every minute
    public void deleteExpiredOrders() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(INTERVAL_MINUTES);
        List<Order> expiredOrders = orderRepository.findByCreatedAtBefore(expirationTime);

        for (Order order : expiredOrders) {
            for (ProductPortion productPortion : order.getProductPortions()) {
                productPortionService.unreserveProductPortion(productPortion);
            }
            orderRepository.delete(order);
        }
    }
}
