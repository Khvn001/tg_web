package com.telegrambot.marketplace.entity.order;

import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.Country;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import com.telegrambot.marketplace.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders",
        indexes = {
                @Index(name = "idx_orders_user_id", columnList = "user_id"),
                @Index(name = "idx_orders_product_id", columnList = "product_id"),
                @Index(name = "idx_orders_product_subcategory_id", columnList = "product_subcategory_id"),
                @Index(name = "idx_orders_product_category_id", columnList = "product_category_id"),
                @Index(name = "idx_orders_country_id", columnList = "country_id"),
                @Index(name = "idx_orders_city_id", columnList = "city_id"),
                @Index(name = "idx_orders_district_id", columnList = "district_id"),
                @Index(name = "idx_orders_basket_id", columnList = "basket_id")
        })
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_subcategory_id")
    private ProductSubcategory productSubcategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "total_sum", nullable = false)
    private BigDecimal totalSum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "basket_id")
    @ToString.Exclude
    private Basket basket;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ProductPortion> productPortions = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
