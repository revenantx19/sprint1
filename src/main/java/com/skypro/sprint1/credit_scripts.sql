--пункт 1
SELECT DISTINCT
    t.product_id, p.type
FROM
    transactions t
JOIN
    products p ON t.product_id = p.id
WHERE
    t.user_id = :userId
    AND p.type = 'CREDIT';

--пункт 2 (1 часть)
SELECT
    t.product_id,
    SUM(t.amount) AS total
FROM
    transactions t
        JOIN
    products p ON t.product_id = p.id
WHERE
    t.user_id = :userId
  AND t.type = 'DEPOSIT'
  AND p.type = 'DEBIT'
GROUP BY
    t.product_id;

--пункт 2 (2 часть)
SELECT
    t.product_id,
    SUM(t.amount) AS total
FROM
    transactions t
        JOIN
    products p ON t.product_id = p.id
WHERE
    t.user_id = :userId
  AND t.type = 'WITHDRAWAL'
  AND p.type = 'DEBIT'
GROUP BY
    t.product_id;

--пункт 3
SELECT
    t.product_id,
    SUM(t.amount) AS total
FROM
    transactions t
JOIN
    products p ON t.product_id = p.id
WHERE
    t.user_id = :userId
    AND t.type = 'WITHDRAWAL'
    AND p.type = 'DEBIT'
GROUP BY
    t.product_id
HAVING SUM(t.amount) > 100000;