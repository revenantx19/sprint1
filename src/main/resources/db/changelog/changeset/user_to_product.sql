-- liquibase formatted sql

-- changeset nikita_malinkin:5
CREATE TABLE user_to_product(
    user_id UUID,
    product_id UUID
)