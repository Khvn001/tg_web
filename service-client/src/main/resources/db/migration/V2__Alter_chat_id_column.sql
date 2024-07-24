-- V2__Alter_chat_id_column.sql
ALTER TABLE users DROP COLUMN IF EXISTS chat_id;

ALTER TABLE users ADD COLUMN IF NOT EXISTS chat_id BIGINT NOT NULL;

ALTER TABLE users ADD CONSTRAINT unique_chat_id UNIQUE (chat_id);