package com.telegrambot.marketplace.enums;

public enum ProductCategoryName {
    ORGANIC("Органика"),
    SYNTHETIC("Синтетика");

    private final String category;

    ProductCategoryName(final String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
