package com.skypro.sprint1.repository;

import com.skypro.sprint1.model.PriceSum;
import com.skypro.sprint1.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с Банковскими продуктами.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /** ПРАВИЛА INVEST */

    /**
     * 1. Пользователь использует как минимум 1 продукт с префиксом DEBIT
     */
    @Query(
            value = "SELECT COUNT(utp.product_id) AS product_count " +
                    "FROM user_to_product utp JOIN products p ON utp.product_id = p.id " +
                    "WHERE utp.user_id = :userId AND p.type = 'DEBIT'",
            nativeQuery = true
    )
    Integer findAmountOfDebitProductsOfUser(UUID userId);


    /**
     * 2. Пользователь не использует продукты с префиксом INVEST
     * Производится реверсивный поиск продуктов с префиксом INVEST. Т.е. сколько продуктов с префиксом INVEST использует пользователь.
     * Далее в Java-коде будет производиться проверка значение. Будет ли оно равно оно нулю или нет.
     */
    @Query(
            value = "SELECT COUNT(utp.product_id) AS product_count " +
                    "FROM user_to_product utp JOIN products p ON utp.product_id = p.id " +
                    "WHERE utp.user_id = :userId AND p.type = 'INVEST'",
            nativeQuery = true
    )
    Integer findAmountOfInvestProductsOfUser(UUID userId);


    /**
     * 3. Пользователь пополнял любой из своих продуктов с префиксом SAVING
     * на суммы до 1000 ₽ за одну операцию в любом месяце.
     */
    @Query(
            value = "SELECT COUNT(t.product_id) AS product_count FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = :userId AND p.type = 'SAVING' " +
                    "AND t.type = 'DEPOSIT' AND amount <= 1000",
            nativeQuery = true
    )
    Integer findAmountOfDepositSavingProductsOfUser(UUID userId);

    /** ПРАВИЛА SAVING */

    /**
     * 1. Пользователь использует как минимум 1 продукт с типом DEBIT (смотри пункт 1 INVEST)
     * 2. У пользователя есть минимум 5 операций пополнения на любой продукт типа DEBIT
     * или SAVING больше чем на 10 000 ₽ за одну операцию.
     */
    @Query(nativeQuery = true,
            value = "SELECT COUNT(t.product_id) AS product_count FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = :userId AND t.type = 'DEPOSIT' " +
                    "AND (p.type = 'DEBIT' OR p.type = 'SAVING') AND t.amount > 10000"
    )
    Integer findAmountOfInvestedDebitOrSavingProductsMoreThan10000(UUID userId);


    /** 3. Сумма пополнений по всем продуктам типа DEBIT > суммы трат по тем же продуктам. */

    /**
     * 3.1 Производится поиск суммы пополнений по каждому продукту
     */
    @Query(
            nativeQuery = true,
            value = "SELECT t.product_id AS productId, SUM(t.amount) AS productSum FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = :userId AND t.type = 'DEPOSIT' AND p.type = 'DEBIT' " +
                    "GROUP BY t.product_id"
    )
    List<PriceSum> findDebitDepositSumByProduct(UUID userId);

    /**
     * 3.2 Производится поиск суммы трат
     */
    @Query(
            nativeQuery = true,
            value = "SELECT t.product_id AS productId, SUM(t.amount) AS productSum FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = :userId AND t.type = 'WITHDRAWAL' AND p.type = 'DEBIT' " +
                    "GROUP BY t.product_id"
    )
    List<PriceSum> findDebitWithdrawalSumByProduct(UUID userId);

    /** ПРАВИЛА CREDIT */

    /**
     * 1. Производиться реверсивный поиск продуктов с префиксом CREDIT. Т.е. сколько продуктов с префиксом INVEST использует пользователь.
     * Далее в Java-коде будет производиться проверка значение. Будет ли оно равно оно нулю или нет.
     */

    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(utp.product_id) AS product_count " +
                    "FROM user_to_product utp JOIN products p ON utp.product_id = p.id " +
                    "WHERE utp.user_id = :userId AND p.type = 'CREDIT'"
    )
    Integer findCreditProductsByUser(UUID userId);

    /**
     * 2. Сумма пополнений по всем продуктам типа DEBIT > суммы трат по тем же продуктам.
     * Необходимо вернуться к пункту 3 типа SAVING
     * 3. Сумма трат по всем продуктам типа DEBIT > 100 000 ₽.
     * Производится поиск общей суммы по данному условию, и в коде буду проверять, больше ли она 100000
     */
    @Query(
            nativeQuery = true,
            value = "SELECT SUM(t.amount) AS total_withdrawal_sum FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = :userId AND p.type = 'DEBIT' AND t.type = 'WITHDRAWAL'"
    )
    Long findSumOfDebitWithdrawalProductsByUser(UUID userId);

}
