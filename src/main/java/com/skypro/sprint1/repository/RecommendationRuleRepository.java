package com.skypro.sprint1.repository;

import com.skypro.sprint1.model.PriceSum;
import com.skypro.sprint1.model.RecommendationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с правилами рекомендаций.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Repository
public interface RecommendationRuleRepository extends JpaRepository<RecommendationRule, UUID> {

    /**
     * 1. Пользователь использует продукт
     */
    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(*) FROM user_to_product utp " +
                    "JOIN products p ON utp.product_id = p.id " +
                    "WHERE utp.user_id = :userId AND p.type = :productType"
    )
    Integer userOf(UUID userId, String productType);

    /** 2. Пользователь не использует продукт (обратная ситуация пункта 1) */


    /**
     * 3. Запрос пополнения. Пользователь пополнял продукт нужного типа на сумму, указанную в запросе.
     */
    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(*) FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = :userId AND t.amount = :sum " +
                    "AND p.type = :productType AND t.type = 'DEPOSIT'"
    )
    Integer topup(UUID userId, String sum, String productType);


    /**
     * 4. Сумма пополнения больше суммы трат. Запрос с указанным типом продукта.
     * Общий метод подсчета суммы как по пополнениям, так и по снятию. Введена дополнительная переменная для типа транзакции (Deposit/Withdrawal)
     */
    @Query(
            nativeQuery = true,
            value = "SELECT t.product_id AS productId, SUM(t.amount) AS productSum " +
                    "FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = :userId AND p.type = :productType AND t.type = :transactionType " +
                    "GROUP BY t.product_id"
    )
    List<PriceSum> totalSum(UUID userId, String productType, String transactionType);


    /**
     * 5. Сумма трат/пополнений по продукту больше некоторой константы N.
     * Введена дополнительная переменная для типа транзакции (Deposit/Withdrawal)
     */
    @Query(
            nativeQuery = true,
            value = "SELECT SUM(t.amount) FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = :userId " +
                    "AND p.type = :productType AND t.type = :transactionType"
    )
    Long totalSumByProduct(UUID userId, String productType, String transactionType);


    /**
     * 7. Пользователь активно использует продукт определенного типа. Запрос, в котором содержится тип продукта.
     */
    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(*) FROM transactions t " +
                    "JOIN products p ON t.product_id = p.id " +
                    "WHERE t.user_id = :userId AND p.type = :productType"
    )
    Integer activeUserOf(UUID userId, String productType);

}
