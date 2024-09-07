-- liquibase formatted sql

-- changeset nikita_malinkin:3
CREATE TABLE products(
        id UUID PRIMARY KEY NOT NULL,
        type VARCHAR(50),
        name VARCHAR(200)
)
