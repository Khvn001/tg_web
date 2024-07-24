-- V3__Add_fields.sql

ALTER TABLE IF EXISTS product_portions
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE IF EXISTS product_portions
    ADD COLUMN user_id BIGINT;
-- Add a foreign key constraint for the user_id column
ALTER TABLE IF EXISTS product_portions
    ADD CONSTRAINT fk_product_portions_users FOREIGN KEY (user_id) REFERENCES users(id);
