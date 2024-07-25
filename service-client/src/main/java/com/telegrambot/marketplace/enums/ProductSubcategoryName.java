package com.telegrambot.marketplace.enums;

import lombok.Getter;

@Getter
public enum ProductSubcategoryName {
    CANNABIS("Марихуана"),
    COCAINE("Кокаин"),
    OPIOIDS("Опиаты"),
    HEROIN("Героин"),
    MDMA("MDMA (Экстази)"),
    METHEDRONE("Мефедрон"),
    METHAMPHETAMINE("Метамфетамин"),
    AMPHETAMINE("Амфетамин"),
    LSD("LSD (Марка)"),
    PSILOCYBIN("Псилоцибин(Грибы)"),
    BENZODIAZEPINES("Бензодиазепины"),
    KETAMINE("Кетамин"),
    PCP("PCP (Фенциклидин)"),
    GHB("GHB (Бутираты)"),
    INHALANTS("Inhalants");

    private final String subcategory;

    ProductSubcategoryName(final String subcategory) {
        this.subcategory = subcategory;
    }

}
