package com.telegrambot.marketplace.entity.user;

import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "user_subcategory_discounts",
        indexes = {
                @Index(name = "idx_user_subcategory_discounts_user_id", columnList = "user_id"),
                @Index(name = "idx_user_subcategory_discounts_product_subcategory_id",
                        columnList = "product_subcategory_id")
        })
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserSubcategoryDiscount {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_subcategory_id", nullable = false)
    @ToString.Exclude
    private ProductSubcategory productSubcategory;

    @Column(nullable = false)
    private BigDecimal discount;

}
