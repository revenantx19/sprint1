package com.skypro.sprint1.model;

import com.skypro.sprint1.repository.RecommendationRuleRepository;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Класс, отвечающий за выполнение правил рекомендаций.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */

@Component
public class RuleExecutioner {

    private final RecommendationRuleRepository recommendationRuleRepository;

    public RuleExecutioner(RecommendationRuleRepository recommendationRuleRepository) {
        this.recommendationRuleRepository = recommendationRuleRepository;
    }

    /**
     * Выполняет заданную группу правил рекомендаций.
     *
     * @param userId идентификатор пользователя.
     * @param rule   Группа правил рекомендаций, разделенных символом "%".
     * @return true, если все правила выполнены, иначе false.
     */

    public boolean execute(UUID userId, String rule) {
        String[] rules = rule.split("%");
        for (String r : rules) {
            String[] ruleArgs = r.split(":");

            boolean valid = switch (ruleArgs[0]) {
                case "userOf" -> userOf(userId, ruleArgs[1]);
                case "notUserOf" -> !userOf(userId, ruleArgs[1]);
                case "topup" -> topup(userId, ruleArgs[1], ruleArgs[2]);
                case "topupGTSpend" -> topupGTSpend(userId, ruleArgs[1]);
                case "spendSGT" -> checkTotalSum(userId, ruleArgs[1], ruleArgs[2], "WITHDRAWAL");
                case "topupSGT" -> checkTotalSum(userId, ruleArgs[1], ruleArgs[2], "DEPOSIT");
                case "activeUserOf" -> activeUserOf(userId, ruleArgs[1]);
                default -> false;
            };

            if (!valid) {
                return false;
            }
        }
        return true;

    }

    /**
     * Проверяет, есть ли у пользователя определенный тип продукта.
     *
     * @param userId      идентификатор пользователя.
     * @param productType Тип продукта.
     * @return true, если пользователь является пользователем этого продукта, иначе false.
     */
    private boolean userOf(UUID userId, String productType) {
        return recommendationRuleRepository.userOf(userId, productType) > 0;
    }

    /**
     * Проверяет, было ли у пользователя пополнение на определенную сумму на конкретный тип продукта.
     *
     * @param userId      идентификатор пользователя.
     * @param sum         Сумма пополнения.
     * @param productType Тип продукта.
     * @return true, если был пополнение, иначе false.
     */

    private boolean topup(UUID userId, String sum, String productType) {
        return recommendationRuleRepository.topup(userId, sum, productType) > 0;
    }

    /**
     * Проверяет, было ли у пользователя пополнение на определенный тип продукта, превышающее сумму вывода средств.
     *
     * @param userId      идентификатор пользователя.
     * @param productType Тип продукта.
     * @return true, если сумма пополнения превышает сумму вывода, иначе false.
     */
    private boolean topupGTSpend(UUID userId, String productType) {
        List<PriceSum> deposit = recommendationRuleRepository.totalSum(userId, productType, "DEPOSIT");
        List<PriceSum> withdrawal = recommendationRuleRepository.totalSum(userId, productType, "WITHDRAWAL");

        Map<UUID, Long> depositMap = new HashMap<>();
        Map<UUID, Long> withdrawalMap = new HashMap<>();

        for (PriceSum priceSum : deposit) {
            depositMap.put(priceSum.getProductId(), priceSum.getProductSum());
        }

        for (PriceSum priceSum : withdrawal) {
            withdrawalMap.put(priceSum.getProductId(), priceSum.getProductSum());
        }

        long totalSum = 0L;

        for (Map.Entry<UUID, Long> entry : depositMap.entrySet()) {
            if (withdrawalMap.containsKey(entry.getKey())) {
                long depositSum = entry.getValue();
                long withdrawalSum = withdrawalMap.get(entry.getKey());
                totalSum += depositSum - withdrawalSum;
            }
        }

        return totalSum > 0;
    }

    /**
     * Проверяет, превышает ли суммарная сумма операций определенного типа на определенный тип продукта заданное значение.
     *
     * @param userId          идентификатор пользователя.
     * @param sum             Сумма, которую нужно превзойти.
     * @param productType     Тип продукта.
     * @param transactionType Тип операции (DEPOSIT/WITHDRAWAL).
     * @return true, если сумма операций превышает заданное значение, иначе false.
     */

    private boolean checkTotalSum(UUID userId, String sum, String productType, String transactionType) {
        return recommendationRuleRepository.totalSumByProduct(userId, productType, transactionType) > Long.parseLong(sum);
    }

    /**
     * Проверяет, является ли пользователь активным пользователем определенного типа продукта.
     *
     * @param userId      идентификатор пользователя.
     * @param productType Тип продукта.
     * @return true, если пользователь является активным пользователем этого продукта, иначе false.
     */

    private boolean activeUserOf(UUID userId, String productType) {
        return recommendationRuleRepository.activeUserOf(userId, productType) > 10;
    }

}
