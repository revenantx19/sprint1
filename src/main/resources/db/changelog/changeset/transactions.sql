-- liquibase formatted sql

-- changeset nikita_malinkin:4
CREATE TABLE transactions(
    id UUID PRIMARY KEY NOT NULL,
    type varchar(50),
    user_id UUID,
    product_id UUID,
    amount INT
)