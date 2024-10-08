-- liquibase formatted sql

-- changeset nikita_malinkin:1
CREATE TABLE users(
    id UUID PRIMARY KEY NOT NULL,
    registration_date TIMESTAMP WITH TIME ZONE,
    user_name VARCHAR(70),
    first_name VARCHAR(50),
    last_name VARCHAR(50)
)
