ALTER TABLE product_portions
    ADD CONSTRAINT fk_product_portion_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id)
            ON DELETE SET NULL;