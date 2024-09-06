--пункт 1
SELECT DISTINCT
    t.product_id, p.type
FROM
    transactions t
JOIN
    products p ON t.product_id = p.id
WHERE
    t.user_id = :userId
    AND p.type = 'DEBIT';

--пункт 2
SELECT DISTINCT
    t.product_id, p.type
FROM
    transactions t
JOIN
    products p ON t.product_id = p.id
WHERE
    t.user_id = :userId
    AND p.type = 'INVEST';

--пункт 3
SELECT
    t.product_id, p.type, t.type, amount
FROM transactions t
JOIN
    products p ON t.product_id = p.id
WHERE
    user_id = :userId
    AND p.type = 'SAVING'
    AND t.type = 'DEPOSIT'
    AND amount <= 1000