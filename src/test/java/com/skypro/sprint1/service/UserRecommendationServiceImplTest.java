package com.skypro.sprint1.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.skypro.sprint1.model.PriceSum;

import static org.assertj.core.api.Assertions.assertThat;

import com.skypro.sprint1.model.RecommendationRule;
import com.skypro.sprint1.model.RuleExecutioner;
import com.skypro.sprint1.pojo.Recommendation;
import com.skypro.sprint1.repository.ProductRepository;
import com.skypro.sprint1.repository.RecommendationRuleRepository;
import com.skypro.sprint1.service.impl.UserRecommendationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Тестовый класс для {@link UserRecommendationServiceImpl}.
 *
 * @author Vladimir Kuznetsov
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class UserRecommendationServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RecommendationRuleRepository ruleRepository;

    @Mock
    private RuleExecutioner ruleExecutioner;

    @InjectMocks
    private UserRecommendationServiceImpl userRecommendationService;

    private UUID userId;

    /**
     * Метод, который запускается перед каждым тестом.
     */
    @BeforeEach
    public void setup() {
        userId = UUID.randomUUID();
    }

    /**
     * Тест, проверяющий, что если нет правил, метод возвращает пустой список и не вызывает {@link RuleExecutioner}.
     */
    @Test
    public void testGetRecommendationsByRules() {
        when(ruleRepository.findAll()).thenReturn(List.of());

        List<Recommendation> recommendations = userRecommendationService.getRecommendationsByRules(UUID.randomUUID());

        assertThat(recommendations).isEmpty();
        verify(ruleExecutioner, never()).execute(any(), any());
    }

    /**
     * Тест, проверяющий, что если правило не проходит проверку, рекомендация не добавляется в список.
     */
    @Test
    public void testGetRecommendationsByRules_RuleExecutesFalse_DoesNotAddRecommendation() {
        RecommendationRule rule = new RecommendationRule();
        when(ruleRepository.findAll()).thenReturn(List.of(rule));
        when(ruleExecutioner.execute(any(), eq(rule.getRule()))).thenReturn(false);

        List<Recommendation> recommendations = userRecommendationService.getRecommendationsByRules(UUID.randomUUID());

        assertThat(recommendations).isEmpty();
        verify(ruleExecutioner).execute(any(), eq(rule.getRule()));
    }

    /**
     * Тест, проверяющий, что метод {@link UserRecommendationServiceImpl#formRecommendation(RecommendationRule)}
     * корректно создает объект {@link Recommendation} на основе переданного {@link RecommendationRule}.
     */
    @Test
    public void testFormRecommendation() {
        RecommendationRule rule = new RecommendationRule("rule1", UUID.randomUUID(), "product1", "description1");

        Recommendation recommendation = userRecommendationService.formRecommendation(rule);

        assertNotNull(recommendation);
        assertEquals("product1", recommendation.getProductName());
        assertEquals(rule.getProductId(), recommendation.getProductId());
        assertEquals("description1", recommendation.getProductDescription());
    }

    /**
     * Тест, проверяющий, что метод {@link UserRecommendationServiceImpl#isUserHaveDebitProduct(UUID)} возвращает true.
     */
    @Test
    public void UserHasDebitProduct() {
        when(productRepository.findAmountOfDebitProductsOfUser(userId)).thenReturn(1);

        boolean hasDebitProduct = userRecommendationService.isUserHaveDebitProduct(userId);

        assertTrue(hasDebitProduct);
        verify(productRepository, times(1)).findAmountOfDebitProductsOfUser(userId);
    }

    /**
     * Тест, проверяющий, что метод {@link UserRecommendationServiceImpl#isUserHaveDebitProduct(UUID)} возвращает false.
     */
    @Test
    public void UserDoesNotHaveDebitProduct() {
        when(productRepository.findAmountOfDebitProductsOfUser(userId)).thenReturn(0);

        boolean hasDebitProduct = userRecommendationService.isUserHaveDebitProduct(userId);

        assertFalse(hasDebitProduct);
        verify(productRepository, times(1)).findAmountOfDebitProductsOfUser(userId);
    }

    /**
     * Проверяет, что метод {@link UserRecommendationServiceImpl#isUserHaveInvestProduct(UUID)} возвращает {@code true},
     * когда пользователь имеет инвестиционные продукты.
     */
    @Test
    public void UserHasInvestProduct() {
        when(productRepository.findAmountOfInvestProductsOfUser(userId)).thenReturn(1);

        boolean hasInvestProduct = userRecommendationService.isUserHaveInvestProduct(userId);

        assertTrue(hasInvestProduct);
        verify(productRepository, times(1)).findAmountOfInvestProductsOfUser(userId);
    }

    /**
     * Проверяет, что метод {@link UserRecommendationServiceImpl#isUserHaveInvestProduct(UUID)} возвращает {@code false},
     * когда пользователь не имеет инвестиционных продуктов.
     */
    @Test
    public void UserDoesNotHaveInvestProduct() {
        when(productRepository.findAmountOfInvestProductsOfUser(userId)).thenReturn(0);

        boolean hasInvestProduct = userRecommendationService.isUserHaveInvestProduct(userId);

        assertFalse(hasInvestProduct);
        verify(productRepository, times(1)).findAmountOfInvestProductsOfUser(userId);
    }

    /**
     * Проверяет, что метод {@link UserRecommendationServiceImpl#isUserDepositInSavingProductPerOperationMoreThan1000(UUID)}
     * возвращает {@code true}, когда пользователь имеет сберегательные продукты.
     */
    @Test
    public void UserHasSavingProduct() {
        when(productRepository.findAmountOfDepositSavingProductsOfUser(userId)).thenReturn(1);

        boolean hasSavingProduct = userRecommendationService.isUserDepositInSavingProductPerOperationMoreThan1000(userId);

        assertTrue(hasSavingProduct);
        verify(productRepository, times(1)).findAmountOfDepositSavingProductsOfUser(userId);
    }

    /**
     * Проверяет, что метод {@link UserRecommendationServiceImpl#isUserDepositInSavingProductPerOperationMoreThan1000(UUID)}
     * возвращает {@code false}, когда пользователь не имеет сберегательных продуктов.
     */
    @Test
    public void UserDoesNotHaveSavingProduct() {
        when(productRepository.findAmountOfDepositSavingProductsOfUser(userId)).thenReturn(0);

        boolean hasSavingProduct = userRecommendationService.isUserDepositInSavingProductPerOperationMoreThan1000(userId);

        assertFalse(hasSavingProduct);
        verify(productRepository, times(1)).findAmountOfDepositSavingProductsOfUser(userId);
    }

    /**
     * Проверяет, что метод {@link UserRecommendationServiceImpl#isUserHaveMoreThanFiveOperationsInDebitOrSavingProductMoreThan10000(UUID)}
     * возвращает {@code true}, когда пользователь имеет не менее 5 операций пополнения
     * на любой продукт типа DEBIT или SAVING больше чем на 10 000 ₽ за одну операцию.
     */
    @Test
    public void UserHasMoreThanFive() {
        when(productRepository.findAmountOfInvestedDebitOrSavingProductsMoreThan10000(userId)).thenReturn(5);

        boolean hasMoreThanFiveOperations = userRecommendationService.isUserHaveMoreThanFiveOperationsInDebitOrSavingProductMoreThan10000(userId);

        assertTrue(hasMoreThanFiveOperations);
        verify(productRepository, times(1)).findAmountOfInvestedDebitOrSavingProductsMoreThan10000(userId);
    }

    /**
     * Тест, который проверяет, что метод {@link UserRecommendationServiceImpl#isUserHaveMoreThanFiveOperationsInDebitOrSavingProductMoreThan10000(UUID)}
     * возвращает {@code false}, когда пользователь не имеет минимум 5 операций пополнения на любой продукт типа DEBIT или SAVING
     * больше чем на 10 000 ₽ за одну операцию.
     */
    @Test
    public void UserDoesNotHaveMoreThanFive() {
        when(productRepository.findAmountOfInvestedDebitOrSavingProductsMoreThan10000(userId)).thenReturn(4);

        boolean hasMoreThanFiveOperations = userRecommendationService.isUserHaveMoreThanFiveOperationsInDebitOrSavingProductMoreThan10000(userId);

        assertFalse(hasMoreThanFiveOperations);
        verify(productRepository, times(1)).findAmountOfInvestedDebitOrSavingProductsMoreThan10000(userId);
    }

    /**
     * Тест, который проверяет, что метод {@link UserRecommendationServiceImpl#isUserHaveMoreDebitDepositAmountThanWithdrawalAmountOnSameProducts(UUID)}
     * возвращает {@code false}, когда сумма пополнений по всем продуктам типа DEBIT не превышает сумму трат по тем же продуктам.
     */
    @Test
    public void UserDoesNotHaveMoreDeposit() {
        List<PriceSum> deposit = Arrays.asList(
                new PriceSum() {
                    @Override
                    public UUID getProductId() {
                        return UUID.randomUUID();
                    }

                    @Override
                    public Long getProductSum() {
                        return 1000L;
                    }
                },
                new PriceSum() {
                    @Override
                    public UUID getProductId() {
                        return UUID.randomUUID();
                    }

                    @Override
                    public Long getProductSum() {
                        return 2000L;
                    }
                }
        );
        List<PriceSum> withdrawal = Arrays.asList(
                new PriceSum() {
                    @Override
                    public UUID getProductId() {
                        return UUID.randomUUID();
                    }

                    @Override
                    public Long getProductSum() {
                        return 500L;
                    }
                },
                new PriceSum() {
                    @Override
                    public UUID getProductId() {
                        return UUID.randomUUID();
                    }

                    @Override
                    public Long getProductSum() {
                        return 1000L;
                    }
                }
        );
        when(productRepository.findDebitDepositSumByProduct(userId)).thenReturn(deposit);
        when(productRepository.findDebitWithdrawalSumByProduct(userId)).thenReturn(withdrawal);

        boolean hasMoreDeposit = userRecommendationService.isUserHaveMoreDebitDepositAmountThanWithdrawalAmountOnSameProducts(userId);

        assertFalse(hasMoreDeposit);
        verify(productRepository, times(1)).findDebitDepositSumByProduct(userId);
        verify(productRepository, times(1)).findDebitWithdrawalSumByProduct(userId);
    }

    /**
     * Проверяем, что метод getTotalSum возвращает 0, если входные данные пустые.
     */
    @Test
    void testGetTotalSumWithEmptyInputs() {
        List<PriceSum> withdrawal = new ArrayList<>();
        Map<UUID, Long> depositMap = new HashMap<>();

        long totalSum = UserRecommendationServiceImpl.getTotalSum(withdrawal, depositMap);

        Assertions.assertEquals(0L, totalSum);
    }

    /**
     * Проверяем, что метод isUserHaveCreditProduct возвращает true, когда пользователь имеет кредитный продукт.
     */
    @Test
    void UserHasCreditProduct() {
        Mockito.when(productRepository.findCreditProductsByUser(userId)).thenReturn(1);

        boolean result = userRecommendationService.isUserHaveCreditProduct(userId);

        Assertions.assertTrue(result);
    }

    /**
     * Проверяем, что метод isUserHaveCreditProduct возвращает false, когда пользователь не имеет кредитный продукт.
     */
    @Test
    void UserDoesNotHaveCreditProduct() {
        Mockito.when(productRepository.findCreditProductsByUser(userId)).thenReturn(0);

        boolean result = userRecommendationService.isUserHaveCreditProduct(userId);

        Assertions.assertFalse(result);
    }

    /**
     * Проверяем, что метод isUserHaveCreditProduct возвращает false, когда пользователь не имеет кредитный продукт.
     */
    @Test
    void SumIsMoreThan100000() {
        Mockito.when(productRepository.findSumOfDebitWithdrawalProductsByUser(userId)).thenReturn(150000L);

        boolean result = userRecommendationService.isWithdrawalSumOnDebitProductsMoreThan100000(userId);

        Assertions.assertTrue(result);
    }

    /**
     * Проверяем, что метод isWithdrawalSumOnDebitProductsMoreThan100000 возвращает false, когда сумма списаний по дебетовым продуктам меньше 100 000.
     */
    @Test
    void SumIsLessThan100000() {
        Mockito.when(productRepository.findSumOfDebitWithdrawalProductsByUser(userId)).thenReturn(50000L);

        boolean result = userRecommendationService.isWithdrawalSumOnDebitProductsMoreThan100000(userId);

        Assertions.assertFalse(result);
    }
}