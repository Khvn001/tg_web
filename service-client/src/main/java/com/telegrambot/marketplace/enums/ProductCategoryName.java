package com.telegrambot.marketplace.enums;

import lombok.Getter;

@Getter
public enum ProductCategoryName {
    ORGANIC("Органика"),
    SYNTHETIC("Синтетика");

    private final String category;

    ProductCategoryName(final String category) {
        this.category = category;
    }

}
