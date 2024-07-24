package com.telegrambot.marketplace.entity.location;

import com.telegrambot.marketplace.entity.inventory.ProductInventoryDistrict;
import com.telegrambot.marketplace.entity.inventory.ProductPortion;
import com.telegrambot.marketplace.entity.order.Order;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "districts",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"country_id", "city_id", "name"})},
        indexes = {
                @Index(name = "idx_districts_country_id", columnList = "country_id"),
                @Index(name = "idx_districts_city_id", columnList = "city_id")
        })
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class District {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    @ToString.Exclude
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    @ToString.Exclude
    private City city;

    @Column(name = "is_allowed", nullable = false)
    private boolean allowed;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<ProductInventoryDistrict> productInventoryDistrictList = new ArrayList<>();

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<ProductPortion> productPortions = new ArrayList<>();

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Order> orders = new ArrayList<>();
}
