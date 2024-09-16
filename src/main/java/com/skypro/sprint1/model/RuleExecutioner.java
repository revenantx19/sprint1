package com.skypro.sprint1.model;

import com.skypro.sprint1.repository.RecommendationRuleRepository;
import org.springframework.stereotype.Component;
import static com.skypro.sprint1.model.Transaction.TransactionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class RuleExecutioner {

    private final RecommendationRuleRepository recommendationRuleRepository;

    public RuleExecutioner(RecommendationRuleRepository recommendationRuleRepository) {
        this.recommendationRuleRepository = recommendationRuleRepository;
    }

    public boolean execute(UUID userId, String rule) {
        String[] ruleArgs = rule.split(":");

        return switch (ruleArgs[0]) {
            case "userOf" -> userOf(userId, ruleArgs[1]);
            case "notUserOf" -> !userOf(userId, ruleArgs[1]);
            case "topup" -> topup(userId, ruleArgs[1], ruleArgs[2]);
            case "topupGTSpend" -> topupGTSpend(userId, ruleArgs[1]);
            case "spendSGT" -> checkTotalSum(userId, ruleArgs[1], ruleArgs[2], TransactionType.WITHDRAWAL);
            case "topupSGT" -> checkTotalSum(userId, ruleArgs[1], ruleArgs[2], TransactionType.DEPOSIT);
            case "activeUserOf" -> activeUserOf(userId, ruleArgs[1]);
            default -> false;
        };
    }


    private boolean userOf(UUID userId, String productType) {
        return recommendationRuleRepository.userOf(userId, productType) > 0;
    }

    private boolean topup(UUID userId, String sum, String productType) {
        return recommendationRuleRepository.topup(userId, sum, productType) > 0;
    }

    private boolean topupGTSpend(UUID userId, String productType) {
        List<PriceSum> deposit = recommendationRuleRepository.totalSum(userId, productType, TransactionType.DEPOSIT.name());
        List<PriceSum> withdrawal = recommendationRuleRepository.totalSum(userId, productType, TransactionType.WITHDRAWAL.name());

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

    private boolean checkTotalSum(UUID userId, String sum, String productType, TransactionType transactionType) {
        return recommendationRuleRepository.totalSumByProduct(userId, productType, transactionType.name()) > Long.parseLong(sum);
    }

    private boolean activeUserOf(UUID userId, String productType) {
        return recommendationRuleRepository.activeUserOf(userId, productType) > 10;
    }

}
