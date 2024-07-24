package com.telegrambot.marketplace.entity.inventory;

import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.Country;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.entity.product.description.Product;
import com.telegrambot.marketplace.entity.product.description.ProductCategory;
import com.telegrambot.marketplace.entity.product.description.ProductSubcategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "product_inventory_districts",
        uniqueConstraints =
                {@UniqueConstraint(
                        columnNames = {"country_id", "city_id", "district_id",
                                "product_id", "product_subcategory_id"})},
        indexes = {
                @Index(name = "idx_product_id", columnList = "product_id"),
                @Index(name = "idx_product_subcategory_id", columnList = "product_subcategory_id"),
                @Index(name = "idx_product_category_id", columnList = "product_category_id"),
                @Index(name = "idx_district_id", columnList = "district_id"),
                @Index(name = "idx_city_id", columnList = "city_id"),
                @Index(name = "idx_country_id", columnList = "country_id")
        })
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventoryDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_subcategory_id", nullable = false)
    @ToString.Exclude
    private ProductSubcategory productSubcategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", nullable = false)
    @ToString.Exclude
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    @ToString.Exclude
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    @ToString.Exclude
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @ToString.Exclude
    private Country country;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal quantity;
}
