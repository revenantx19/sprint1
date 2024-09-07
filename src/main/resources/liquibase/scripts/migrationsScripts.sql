--liquidbase formated sql

-- changeset rvoroshnin:1
CREATE TABLE product
(
    product_id SERIAL PRIMARY KEY,
    product_type TEXT,
    product_name TEXT
);

CREATE TABLE user
(
    user_id SERIAL PRIMARY KEY,
    registration_date TIMESTAMP WITH TIME ZONE,
    user_name TEXT,
    first_name TEXT,
    last_name TEXT
);

CREATE TABLE user_to_product
(
    user_id INT REFERENCES user (user_id),
    product_id INT REFERENCES product (product_id),
    PRIMARY KEY (user_id, product_id)
);

CREATE TABLE transactions
(
    transaction_id SERIAL PRIMARY KEY,
    transaction_type TEXT,
    user_id INT REFERENCES user (user_id),
    product_id INT REFERENCES product (product_id),
    amount DECIMAL(10, 2)
);