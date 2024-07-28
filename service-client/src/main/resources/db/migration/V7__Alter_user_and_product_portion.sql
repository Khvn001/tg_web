-- Step 1: Drop existing foreign key constraint on user_id in product_portions table
ALTER TABLE product_portions
    DROP CONSTRAINT IF EXISTS fk_product_portions_users;

-- Step 2: Drop the user_id column from product_portions table
ALTER TABLE product_portions
    DROP COLUMN IF EXISTS user_id;

-- Step 3: Add the new courier_id column to product_portions table
ALTER TABLE product_portions
    ADD COLUMN courier_id BIGINT;

-- Step 4: Add the new foreign key constraint for courier_id in product_portions table
ALTER TABLE product_portions
    ADD CONSTRAINT fk_product_portions_courier_id
        FOREIGN KEY (courier_id)
            REFERENCES users(id);

-- Step 5: Drop the existing one-to-one relationship for courierTemporaryProductPortion in users table
ALTER TABLE users
    DROP CONSTRAINT IF EXISTS fk_users_courier_temporary_product_portion;

-- Step 6: Drop the courierTemporaryProductPortion field (if it was previously mapped)
-- This step is typically not needed if the field was not stored in a separate column
-- Adjust according to your actual schema management and ORM behavior

-- Step 7: Ensure the new one-to-many relationship for courierProductPortions in users table
-- This step is typically managed by the ORM and does not require explicit SQL changes
-- Ensure the schema reflects the changes

-- Step 8: (Optional) Add an index for courier_id in product_portions for performance improvement
CREATE INDEX idx_product_portions_courier_id ON product_portions(courier_id);
