package com.skypro.sprint1.service.impl;

import com.skypro.sprint1.model.PriceSum;
import com.skypro.sprint1.model.Product;
import com.skypro.sprint1.model.RecommendationRule;
import com.skypro.sprint1.model.RuleExecutioner;
import com.skypro.sprint1.pojo.Recommendation;
import com.skypro.sprint1.pojo.UserRecommendation;
import com.skypro.sprint1.repository.ProductRepository;
import com.skypro.sprint1.repository.RecommendationRuleRepository;
import com.skypro.sprint1.service.UserRecommendationService;
import com.skypro.sprint1.util.RecommendationMessageUtil;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public Optional<UserRecommendation> getRecommendationsByRules(UUID userId) {
        List<RecommendationRule> rules = ruleRepository.findAll();

        List<Recommendation> recommendations = new ArrayList<>();

        for (RecommendationRule rule : rules) {
            if (ruleExecutioner.execute(userId, rule.getRule())) {
                Recommendation recommendation = formRecommendation(rule);
                recommendations.add(recommendation);
            }
        }

        if (recommendations.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new UserRecommendation(userId, recommendations));
    }

    // Формирование рекомендаций для пользователя
    @Override
    public UserRecommendation getRecommendations(UUID userId) {
        List<Recommendation> recommendations = new ArrayList<>();

        if (recommendInvestProduct(userId)) {
            Product investProduct = getRandomInvestProduct();
            Recommendation recommendation = new Recommendation(
                    investProduct.getName(),
                    investProduct.getId(),
                    RecommendationMessageUtil.INVEST_MESSAGE
            );
            recommendations.add(recommendation);
        }

        if (recommendSavingProduct(userId)) {
            Product savingProduct = getRandomSavingProduct();
            Recommendation recommendation = new Recommendation(
                    savingProduct.getName(),
                    savingProduct.getId(),
                    RecommendationMessageUtil.SAVING_MESSAGE
            );
            recommendations.add(recommendation);
        }

        if (recommendCreditProduct(userId)) {
            Product creditProduct = getRandomCreditProduct();
            Recommendation recommendation = new Recommendation(
                    creditProduct.getName(),
                    creditProduct.getId(),
                    RecommendationMessageUtil.CREDIT_MESSAGE
            );
            recommendations.add(recommendation);
        }

        if (recommendations.isEmpty()) {
            return null;
        }

        return new UserRecommendation(userId, recommendations);

    }

    private Recommendation formRecommendation(RecommendationRule rule) {
        return new Recommendation(rule.getProductName(), rule.getProductId(), rule.getProductDescription());
    }

    // Проверка на то, что пользователь использует продукт типа DEBIT
    private boolean isUserHaveDebitProduct(UUID userId) {
        Integer countDebitProducts = productRepository.findAmountOfDebitProductsOfUser(userId);
        return countDebitProducts > 0;
    }

    // Проверка на то, что пользователь использует продукт типа INVEST
    private boolean isUserHaveInvestProduct(UUID userId) {
        Integer countInvestProducts = productRepository.findAmountOfInvestProductsOfUser(userId);
        return countInvestProducts > 0;
    }

    // Проверка на то, что пользователь пополнял любой из своих продуктов с префиксом SAVING на суммы до 1000 ₽ за одну операцию в любом месяце.
    private boolean isUserDepositInSavingProductPerOperationMoreThan1000(UUID userId) {
        Integer countDepositInSavingProduct = productRepository.findAmountOfDepositSavingProductsOfUser(userId);
        return countDepositInSavingProduct > 0;
    }

    // Проверка на то, что у пользователя есть минимум 5 операций пополнения на любой продукт типа DEBIT или SAVING больше чем на 10 000 ₽ за одну операцию.
    private boolean isUserHaveMoreThanFiveOperationsInDebitOrSavingProductMoreThan10000(UUID userId) {
        Integer countResult = productRepository.findAmountOfInvestedDebitOrSavingProductsMoreThan10000(userId);
        return countResult >= 5;
    }

    //Проверка на то, у пользователя сумма пополнений по всем продуктам типа DEBIT > суммы трат по тем же продуктам
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

    public static long getTotalSum(List<PriceSum> withdrawal, Map<UUID, Long> depositMap) {
        Map<UUID, Long> withdrawalMap = new HashMap<>();

        for (PriceSum priceSum : withdrawal) {
            withdrawalMap.put(priceSum.getProductId(), priceSum.getProductSum());
        }

        long totalSum = 0;

        totalSum = getTotalSum(depositMap, withdrawalMap, totalSum);
        return totalSum;
    }

    public static long getTotalSum(Map<UUID, Long> depositMap, Map<UUID, Long> withdrawalMap, long totalSum) {
        for (Map.Entry<UUID,Long> entry : depositMap.entrySet()) {
            if (withdrawalMap.containsKey(entry.getKey())) {
                long depositAmount = entry.getValue();
                long withdrawalAmount = withdrawalMap.get(entry.getKey());
                totalSum += depositAmount - withdrawalAmount;
            }
        }
        return totalSum;
    }

    // Проверка на то, что пользователь использует продукт типа CREDIT
    private boolean isUserHaveCreditProduct(UUID userId) {
        Integer countDebitProducts = productRepository.findCreditProductsByUser(userId);
        return countDebitProducts > 0;
    }

    // Проверка на то, что сумма трат по всем продуктам типа DEBIT за три месяца > 100 000 ₽.
    private boolean isWithdrawalSumOnDebitProductsMoreThan100000(UUID userId) {
        Long sum = productRepository.findSumOfDebitWithdrawalProductsByUser(userId);
        return sum > 100000;
    }

    // Проверка правил для рекомендации продукта типа INVEST
    private boolean recommendInvestProduct(UUID userId) {
        return isUserHaveDebitProduct(userId)
                && !isUserHaveInvestProduct(userId)
                && isUserDepositInSavingProductPerOperationMoreThan1000(userId);
    }

    // Проверка правил для рекомендации продукта типа SAVING
    private boolean recommendSavingProduct(UUID userId) {
        return isUserHaveDebitProduct(userId)
                && isUserHaveMoreThanFiveOperationsInDebitOrSavingProductMoreThan10000(userId)
                && isUserHaveMoreDebitDepositAmountThanWithdrawalAmountOnSameProducts(userId);
    }

    // Проверка правил для рекомендации продукта типа CREDIT
    private boolean recommendCreditProduct(UUID userId) {
        return !isUserHaveCreditProduct(userId)
                && isUserHaveMoreDebitDepositAmountThanWithdrawalAmountOnSameProducts(userId)
                && isWithdrawalSumOnDebitProductsMoreThan100000(userId);
    }

    // Получение случайного продукта типа INVEST для рекомендации пользователю
    private Product getRandomInvestProduct() {
        List<Product> investProducts = productRepository.findAllInvestProducts();
        Random random = new Random();
        int value = random.nextInt(investProducts.size());
        return investProducts.get(value);
    }

    // Получение случайного продукта типа SAVING для рекомендации пользователю
    private Product getRandomSavingProduct() {
        List<Product> savingProducts = productRepository.findAllSavingProducts();
        Random random = new Random();
        int value = random.nextInt(savingProducts.size());
        return savingProducts.get(value);
    }

    // Получение случайного продукта типа CREDIT для рекомендации пользователю
    private Product getRandomCreditProduct() {
        List<Product> creditProducts = productRepository.findAllCreditProducts();
        Random random = new Random();
        int value = random.nextInt(creditProducts.size());
        return creditProducts.get(value);
    }

}
