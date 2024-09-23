package com.skypro.sprint1.service.impl;

import com.skypro.sprint1.model.PriceSum;
import com.skypro.sprint1.model.RecommendationRule;
import com.skypro.sprint1.model.RuleExecutioner;
import com.skypro.sprint1.pojo.Recommendation;
import com.skypro.sprint1.pojo.UserRecommendation;
import com.skypro.sprint1.repository.ProductRepository;
import com.skypro.sprint1.repository.RecommendationRuleRepository;
import com.skypro.sprint1.service.UserRecommendationService;
import com.skypro.sprint1.util.RecommendationProductUtil;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис, предоставляющий рекомендации пользователям.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Service
public class UserRecommendationServiceImpl implements UserRecommendationService {

    private final ProductRepository productRepository;
    private final RecommendationRuleRepository ruleRepository;
    private final RuleExecutioner ruleExecutioner;

    public UserRecommendationServiceImpl(ProductRepository productRepository,
                                         RecommendationRuleRepository ruleRepository,
                                         RuleExecutioner ruleExecutioner) {
        this.productRepository = productRepository;
        this.ruleRepository = ruleRepository;
        this.ruleExecutioner = ruleExecutioner;
    }


    /**
     * Формирование рекомендаций для пользователя
     *
     * @param userId
     * @return Рекомендации пользователя.
     */
    @Override
    public Optional<UserRecommendation> getRecommendations(UUID userId) {
        List<Recommendation> recommendations = new ArrayList<>();

        if (recommendInvestProduct(userId)) {
            Recommendation recommendation = RecommendationProductUtil.getInvestProduct();
            recommendations.add(recommendation);
        }

        if (recommendSavingProduct(userId)) {
            Recommendation recommendation = RecommendationProductUtil.getSavingProduct();
            recommendations.add(recommendation);
        }

        if (recommendCreditProduct(userId)) {
            Recommendation recommendation = RecommendationProductUtil.getCreditProduct();
            recommendations.add(recommendation);
        }

        recommendations.addAll(getRecommendationsByRules(userId));

        if (recommendations.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new UserRecommendation(userId, recommendations));

    }

    /**
     * Рекомендации по введенным правилам
     *
     * @param userId
     * @return Рекомендации по правилам. Если пользователь не может выполнить правила, то рекомендации не добавляются.
     */
    private List<Recommendation> getRecommendationsByRules(UUID userId) {
        List<RecommendationRule> rules = ruleRepository.findAll();

        List<Recommendation> recommendations = new ArrayList<>();

        for (RecommendationRule rule : rules) {
            if (ruleExecutioner.execute(userId, rule.getRule())) {
                Recommendation recommendation = formRecommendation(rule);
                recommendations.add(recommendation);
            }
        }

        return recommendations;
    }

    /**
     * Формирование рекомендации по правилу.
     *
     * @param rule Правило рекомендации
     * @return
     */
    private Recommendation formRecommendation(RecommendationRule rule) {
        return new Recommendation(rule.getProductName(), rule.getProductId(), rule.getProductDescription());
    }

    /**
     * Проверка на то, что пользователь использует продукт типа DEBIT
     *
     * @param userId
     * @return true, если пользователь использует продукт типа DEBIT. Иначе false.
     */
    private boolean isUserHaveDebitProduct(UUID userId) {
        Integer countDebitProducts = productRepository.findAmountOfDebitProductsOfUser(userId);
        return countDebitProducts > 0;
    }

    /**
     * Проверка на то, что пользователь использует продукт типа INVEST
     *
     * @param userId
     * @return true, если пользователь использует продукт типа INVEST. Иначе false.
     */
    private boolean isUserHaveInvestProduct(UUID userId) {
        Integer countInvestProducts = productRepository.findAmountOfInvestProductsOfUser(userId);
        return countInvestProducts > 0;
    }

    /**
     * Проверка на то, что пользователь пополнял любой из своих продуктов с префиксом SAVING на суммы до 1000 ₽ за одну операцию в любом месяце.
     *
     * @param userId
     * @return {@code true}, если пользователь пополнял такие продукты, {@code false} иначе.
     */
    private boolean isUserDepositInSavingProductPerOperationMoreThan1000(UUID userId) {
        Integer countDepositInSavingProduct = productRepository.findAmountOfDepositSavingProductsOfUser(userId);
        return countDepositInSavingProduct > 0;
    }

    /**
     * Проверяет, есть ли у пользователя минимум 5 операций пополнения на любой продукт типа DEBIT или SAVING на сумму больше чем 10 000 ₽ за одну операцию.
     *
     * @param userId Идентификатор пользователя.
     * @return {@code true}, если у пользователя есть такие операции, {@code false} иначе.
     */
    private boolean isUserHaveMoreThanFiveOperationsInDebitOrSavingProductMoreThan10000(UUID userId) {
        Integer countResult = productRepository.findAmountOfInvestedDebitOrSavingProductsMoreThan10000(userId);
        return countResult >= 5;
    }

    /**
     * Проверяет, превышает ли сумма пополнений по всем продуктам типа DEBIT сумму трат по тем же продуктам у пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return {@code true}, если сумма пополнений превышает сумму трат, {@code false} иначе.
     */
    private boolean isUserHaveMoreDebitDepositAmountThanWithdrawalAmountOnSameProducts(UUID userId) {

        List<PriceSum> deposit = productRepository.findDebitDepositSumByProduct(userId);
        List<PriceSum> withdrawal = productRepository.findDebitWithdrawalSumByProduct(userId);

        Map<UUID, Long> depositMap = new HashMap<>();

        for (PriceSum priceSum : deposit) {
            depositMap.put(priceSum.getProductId(), priceSum.getProductSum());
        }

        long totalSum = getTotalSum(withdrawal, depositMap);

        return totalSum > 0;
    }

    /**
     * Метод для вычисления общей суммы по списку операций вывода и карте депозитов.
     *
     * @param withdrawal Список операций вывода.
     * @param depositMap Карта депозитов.
     * @return Общая сумма.
     */
    public static long getTotalSum(List<PriceSum> withdrawal, Map<UUID, Long> depositMap) {
        Map<UUID, Long> withdrawalMap = new HashMap<>();

        for (PriceSum priceSum : withdrawal) {
            withdrawalMap.put(priceSum.getProductId(), priceSum.getProductSum());
        }

        long totalSum = 0;

        totalSum = getTotalSum(depositMap, withdrawalMap, totalSum);
        return totalSum;
    }

    /**
     * Метод для вычисления общей суммы по картам депозитов и вывода.
     *
     * @param depositMap    Карта депозитов.
     * @param withdrawalMap Карта вывода.
     * @param totalSum      Текущая общая сумма.
     * @return Общая сумма.
     */
    public static long getTotalSum(Map<UUID, Long> depositMap, Map<UUID, Long> withdrawalMap, long totalSum) {
        for (Map.Entry<UUID, Long> entry : depositMap.entrySet()) {
            if (withdrawalMap.containsKey(entry.getKey())) {
                long depositAmount = entry.getValue();
                long withdrawalAmount = withdrawalMap.get(entry.getKey());
                totalSum += depositAmount - withdrawalAmount;
            }
        }
        return totalSum;
    }

    /**
     * Проверка на то, что пользователь использует продукт типа CREDIT
     *
     * @param userId
     * @return true, если у пользователя есть продукт типа CREDIT, иначе false
     */
    private boolean isUserHaveCreditProduct(UUID userId) {
        Integer countDebitProducts = productRepository.findCreditProductsByUser(userId);
        return countDebitProducts > 0;
    }

    /**
     * Проверяет, больше ли сумма трат по всем продуктам типа DEBIT за три месяца 100 000 ₽.
     *
     * @param userId идентификатор пользователя
     * @return true, если сумма трат больше 100 000 ₽, иначе false
     */
    private boolean isWithdrawalSumOnDebitProductsMoreThan100000(UUID userId) {
        Long sum = productRepository.findSumOfDebitWithdrawalProductsByUser(userId);
        return sum > 100000;
    }

    /**
     * Проверяет правила для рекомендации продукта типа INVEST.
     *
     * @param userId идентификатор пользователя
     * @return true, если правила выполнены, иначе false
     */
    private boolean recommendInvestProduct(UUID userId) {
        return isUserHaveDebitProduct(userId)
                && !isUserHaveInvestProduct(userId)
                && isUserDepositInSavingProductPerOperationMoreThan1000(userId);
    }

    /**
     * Проверяет правила для рекомендации продукта типа SAVING.
     *
     * @param userId идентификатор пользователя
     * @return true, если правила выполнены, иначе false
     */
    private boolean recommendSavingProduct(UUID userId) {
        return isUserHaveDebitProduct(userId)
                && isUserHaveMoreThanFiveOperationsInDebitOrSavingProductMoreThan10000(userId)
                && isUserHaveMoreDebitDepositAmountThanWithdrawalAmountOnSameProducts(userId);
    }

    /**
     * Проверяет правила для рекомендации продукта типа CREDIT.
     *
     * @param userId идентификатор пользователя
     * @return true, если правила выполнены, иначе false
     */
    private boolean recommendCreditProduct(UUID userId) {
        return !isUserHaveCreditProduct(userId)
                && isUserHaveMoreDebitDepositAmountThanWithdrawalAmountOnSameProducts(userId)
                && isWithdrawalSumOnDebitProductsMoreThan100000(userId);
    }


}
