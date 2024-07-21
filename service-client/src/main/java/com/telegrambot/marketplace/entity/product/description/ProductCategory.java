package com.telegrambot.marketplace.entity.product.description;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryDistrict;
import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.order.Order;
import com.telegrambot.marketplace.entity.inventory.ProductInventoryCity;
import com.telegrambot.marketplace.enums.ProductCategoryName;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Table(name = "product_categories",
        indexes = {
                @Index(name = "idx_product_categories_name", columnList = "name"),
                @Index(name = "idx_product_categories_is_allowed", columnList = "is_allowed")
        })
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private ProductCategoryName name;

    @Column(name = "is_allowed", nullable = false)
    private boolean allowed;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<ProductSubcategory> productSubcategories = new ArrayList<>();

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<ProductInventoryCity> productInventoryCityList = new ArrayList<>();

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<ProductInventoryDistrict> productInventoryDistrictList = new ArrayList<>();

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<ProductPortion> productPortions = new ArrayList<>();

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Order> orders = new ArrayList<>();

}
