package com.telegrambot.marketplace.entity.product.description;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryDistrict;
import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.inventory.ProductInventoryCity;
import com.telegrambot.marketplace.entity.user.UserSubcategoryDiscount;
import com.telegrambot.marketplace.enums.ProductSubcategoryName;
import jakarta.persistence.CascadeType;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_subcategories",
        indexes = {
                @Index(name = "idx_product_subcategories_name", columnList = "name"),
                @Index(name = "idx_product_subcategories_is_allowed", columnList = "is_allowed")
        })
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductSubcategory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private ProductSubcategoryName name;

    @Column(name = "is_allowed", nullable = false)
    private boolean allowed;

    @OneToMany(mappedBy = "productSubcategory", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "productSubcategory", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    List<ProductInventoryDistrict> productInventoryDistrictList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "productSubcategory", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<ProductInventoryCity> productInventoryCityList = new ArrayList<>();

    @OneToMany(mappedBy = "productSubcategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "productSubcategory", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<UserSubcategoryDiscount> userSubcategoryDiscounts = new ArrayList<>();

    @OneToMany(mappedBy = "productSubcategory", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<ProductPortion> productPortions = new ArrayList<>();
}
