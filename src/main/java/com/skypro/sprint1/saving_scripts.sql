--пункт 1
SELECT t.product_id FROM transactions t
JOIN products p ON t.product_id = p.id
WHERE user_id = :userId AND p.type = 'DEBIT';

--пункт 2 (1 часть условия)
SELECT t.product_id, COUNT(p.type)  FROM transactions t
JOIN products p ON t.product_id = p.id
WHERE user_id = :userId AND p.type = 'DEPOSIT'
GROUP BY t.product_id
HAVING COUNT(p.type) > 5;

--пункт 2 (2 часть условия)
SELECT t.product_id FROM transactions t
JOIN products p ON t.product_id = p.id
WHERE user_id = :userId AND p.type = 'SAVING' AND amount > 10000;

--пункт 3 (такой же как пункт 2 в credit_scripts.sql)
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
