-- V3__Add_order_to_product_portion.sql

ALTER TABLE IF EXISTS product_portions
    ADD COLUMN order_id BIGINT;