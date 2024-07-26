-- V3__Add_price_to_product.sql

ALTER TABLE IF EXISTS products
    ADD COLUMN price BIGINT;