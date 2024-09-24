package com.skypro.sprint1.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.skypro.sprint1.model.PriceSum;
import com.skypro.sprint1.model.RecommendationRule;
import com.skypro.sprint1.model.RuleExecutioner;
import com.skypro.sprint1.pojo.Recommendation;
import com.skypro.sprint1.pojo.UserRecommendation;
import com.skypro.sprint1.repository.ProductRepository;
import com.skypro.sprint1.repository.RecommendationRuleRepository;
import com.skypro.sprint1.service.impl.UserRecommendationServiceImpl;
import com.skypro.sprint1.util.RecommendationProductUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @BeforeEach
    public void setup() {
        userId = UUID.randomUUID();
    }

    //Должен возвращать рекомендации по правилам
    @Test
    public void shouldReturnRecommendationsByRules() {
        RecommendationRule rule1 = new RecommendationRule("rule1", UUID.randomUUID(),"name1", "description1");
        RecommendationRule rule2 = new RecommendationRule("rule2", UUID.randomUUID(), "name2", "description2");
        List<RecommendationRule> rules = Arrays.asList(rule1, rule2);

        when(ruleRepository.findAll()).thenReturn(rules);
        when(ruleExecutioner.execute(userId, rule1.getRule())).thenReturn(true);
        when(ruleExecutioner.execute(userId, rule2.getRule())).thenReturn(false);

        Optional<UserRecommendation> result = userRecommendationService.getRecommendations(userId);

        assertTrue(result.isPresent());
        UserRecommendation userRecommendation = result.get();
        assertEquals(userId, userRecommendation.getUserId());
        assertEquals(1, userRecommendation.getRecommendations().size());
        assertEquals(rule1.getProductName(), userRecommendation.getRecommendations().get(0).getProductName());
        assertEquals(rule1.getProductId(), userRecommendation.getRecommendations().get(0).getProductId());
        assertEquals(rule1.getProductDescription(), userRecommendation.getRecommendations().get(0));
    }

    // Проверяем, что метод возвращает getRecommendations с 4 рекомендациями, когда все продукты доступны
    @Test
    public void testGetRecommendationsWithAllProducts() {
        when(RecommendationProductUtil.getInvestProduct()).thenReturn(new Recommendation());
        when(RecommendationProductUtil.getSavingProduct()).thenReturn(new Recommendation());
        when(RecommendationProductUtil.getCreditProduct()).thenReturn(new Recommendation());

        List<Recommendation> expectedRecommendations = List.of(
                new Recommendation(),
                new Recommendation(),
                new Recommendation(),
                new Recommendation()
        );
        when(userRecommendationService.getRecommendations(userId)).thenReturn(Optional.of(new UserRecommendation(userId, expectedRecommendations)));

        Optional<UserRecommendation> result = userRecommendationService.getRecommendations(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getUserId());
        assertEquals(expectedRecommendations, result.get().getRecommendations());
        assertEquals(4, result.get().getRecommendations().size());
    }

    // Проверяем, что метод возвращает пустой список, когда ни одно правило не соответствует
    @Test
    public void NoRulesMatch() {
        List<RecommendationRule> rules = Arrays.asList(
                new RecommendationRule("rule1", UUID.randomUUID(), "product1", "description1"),
                new RecommendationRule("rule2", UUID.randomUUID(), "product2", "description2"),
                new RecommendationRule("rule3", UUID.randomUUID(), "product3", "description3")
        );
        when(ruleRepository.findAll()).thenReturn(rules);
        when(ruleExecutioner.execute(eq(userId), any())).thenReturn(false);

        Optional<UserRecommendation> recommendations = userRecommendationService.getRecommendations(userId);

        assertTrue(recommendations.isEmpty());
    }

    // Проверяем метод возвращает две рекомендации, когда некоторые правила соответствуют
    @Test
    public void SomeRulesMatch() {
        List<RecommendationRule> rules = Arrays.asList(
                new RecommendationRule("rule1", UUID.randomUUID(), "product1", "description1"),
                new RecommendationRule("rule2", UUID.randomUUID(), "product2", "description2"),
                new RecommendationRule("rule3", UUID.randomUUID(), "product3", "description3")
        );
        when(ruleRepository.findAll()).thenReturn(rules);
        when(ruleExecutioner.execute(eq(userId), any())).thenReturn(true, false, true);

        Optional<UserRecommendation> recommendations = userRecommendationService.getRecommendations(userId);

        assertFalse(recommendations.isEmpty());
        assertEquals(2, recommendations.get().getRecommendations().size());
    }

    //Проверяем, что метод formRecommendation корректно создает объект Recommendation на основе переданного RecommendationRule
    @Test
    public void testFormRecommendation() {
        RecommendationRule rule = new RecommendationRule("rule1", UUID.randomUUID(), "product1", "description1");

        Recommendation recommendation = userRecommendationService.formRecommendation(rule);

        assertNotNull(recommendation);
        assertEquals("product1", recommendation.getProductName());
        assertEquals(rule.getProductId(), recommendation.getProductId());
        assertEquals("description1", recommendation.getProductDescription());
    }

    // Проверяем, что метод  isUserHaveDebitProduct возвращает true
    @Test
    public void UserHasDebitProduct() {
        when(productRepository.findAmountOfDebitProductsOfUser(userId)).thenReturn(1);

        boolean hasDebitProduct = userRecommendationService.isUserHaveDebitProduct(userId);

        assertTrue(hasDebitProduct);
        verify(productRepository, times(1)).findAmountOfDebitProductsOfUser(userId);
    }

    // Проверяем, что метод  isUserHaveDebitProduct возвращает false
    @Test
    public void UserDoesNotHaveDebitProduct() {
        when(productRepository.findAmountOfDebitProductsOfUser(userId)).thenReturn(0);

        boolean hasDebitProduct = userRecommendationService.isUserHaveDebitProduct(userId);

        assertFalse(hasDebitProduct);
        verify(productRepository, times(1)).findAmountOfDebitProductsOfUser(userId);
    }

    // Проверяем, что метод  isUserHaveInvestProduct возвращает true
    @Test
    public void UserHasInvestProduct() {
        when(productRepository.findAmountOfInvestProductsOfUser(userId)).thenReturn(1);

        boolean hasInvestProduct = userRecommendationService.isUserHaveInvestProduct(userId);

        assertTrue(hasInvestProduct);
        verify(productRepository, times(1)).findAmountOfInvestProductsOfUser(userId);
    }

    // Проверяем, что метод  isUserHaveInvestProduct возвращает false
    @Test
    public void UserDoesNotHaveInvestProduct() {
        when(productRepository.findAmountOfInvestProductsOfUser(userId)).thenReturn(0);

        boolean hasInvestProduct = userRecommendationService.isUserHaveInvestProduct(userId);

        assertFalse(hasInvestProduct);
        verify(productRepository, times(1)).findAmountOfInvestProductsOfUser(userId);
    }

    // Проверяем, что метод  isUserDepositInSavingProductPerOperationMoreThan1000 возвращает true
    @Test
    public void UserHasSavingProduct() {
        when(productRepository.findAmountOfDepositSavingProductsOfUser(userId)).thenReturn(1);

        boolean hasSavingProduct = userRecommendationService.isUserDepositInSavingProductPerOperationMoreThan1000(userId);

        assertTrue(hasSavingProduct);
        verify(productRepository, times(1)).findAmountOfDepositSavingProductsOfUser(userId);
    }

    // Проверяем, что метод  isUserDepositInSavingProductPerOperationMoreThan1000 возвращает false
    @Test
    public void UserDoesNotHaveSavingProduct() {
        when(productRepository.findAmountOfDepositSavingProductsOfUser(userId)).thenReturn(0);

        boolean hasSavingProduct = userRecommendationService.isUserDepositInSavingProductPerOperationMoreThan1000(userId);

        assertFalse(hasSavingProduct);
        verify(productRepository, times(1)).findAmountOfDepositSavingProductsOfUser(userId);
    }

    // Проверяем, что метод  isUserDepositInSavingProductPerOperationMoreThan1000 возвращает true, когда пользователь имеет минимум 5 операций пополнения
    // на любой продукт типа DEBIT или SAVING больше чем на 10 000 ₽ за одну операцию
    @Test
    public void UserHasMoreThanFive() {
        when(productRepository.findAmountOfInvestedDebitOrSavingProductsMoreThan10000(userId)).thenReturn(5);

        boolean hasMoreThanFiveOperations = userRecommendationService.isUserHaveMoreThanFiveOperationsInDebitOrSavingProductMoreThan10000(userId);

        assertTrue(hasMoreThanFiveOperations);
        verify(productRepository, times(1)).findAmountOfInvestedDebitOrSavingProductsMoreThan10000(userId);
    }

    // Проверяем, что метод  isUserDepositInSavingProductPerOperationMoreThan1000 возвращает false, когда пользователь не имеет минимум 5 операций пополнения
    // на любой продукт типа DEBIT или SAVING больше чем на 10 000 ₽ за одну операцию
    @Test
    public void UserDoesNotHaveMoreThanFive() {
        when(productRepository.findAmountOfInvestedDebitOrSavingProductsMoreThan10000(userId)).thenReturn(4);

        boolean hasMoreThanFiveOperations = userRecommendationService.isUserHaveMoreThanFiveOperationsInDebitOrSavingProductMoreThan10000(userId);

        assertFalse(hasMoreThanFiveOperations);
        verify(productRepository, times(1)).findAmountOfInvestedDebitOrSavingProductsMoreThan10000(userId);
    }

    // Проверяет, что метод возвращает true, когда сумма пополнений по всем продуктам типа DEBIT превышает сумму трат по тем же продуктам
    @Test
    public void UserHasMoreDeposit() {
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

        assertTrue(hasMoreDeposit);
        verify(productRepository, times(1)).findDebitDepositSumByProduct(userId);
        verify(productRepository, times(1)).findDebitWithdrawalSumByProduct(userId);
    }

    // Проверяем, что метод возвращает false, когда сумма пополнений по всем продуктам типа DEBIT не превышает сумму трат по тем же продуктам
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

    // Проверяем, что метод getTotalSum правильно рассчитывает общую сумму при наличии корректных входных данных
    @Test
    void testGetTotalSumWithValidInputs() {
        List<PriceSum> withdrawal = new ArrayList<>();
        withdrawal.add(new PriceSum() {
            @Override
            public UUID getProductId() {
                return UUID.randomUUID();
            }

            @Override
            public Long getProductSum() {
                return 100L;
            }
        });
        withdrawal.add(new PriceSum() {
            @Override
            public UUID getProductId() {
                return UUID.randomUUID();
            }

            @Override
            public Long getProductSum() {
                return 50L;
            }
        });
        withdrawal.add(new PriceSum() {
            @Override
            public UUID getProductId() {
                return UUID.randomUUID();
            }

            @Override
            public Long getProductSum() {
                return 75L;
            }
        });

        Map<UUID, Long> depositMap = new HashMap<>();
        depositMap.put(withdrawal.get(0).getProductId(), 200L);
        depositMap.put(withdrawal.get(1).getProductId(), 100L);
        depositMap.put(withdrawal.get(2).getProductId(), 150L);

        long totalSum = UserRecommendationServiceImpl.getTotalSum(withdrawal, depositMap);

        Assertions.assertEquals(225L, totalSum);
    }

    // Проверяем, что метод getTotalSum возвращает 0, если входные данные пустые
    @Test
    void testGetTotalSumWithEmptyInputs() {
        List<PriceSum> withdrawal = new ArrayList<>();
        Map<UUID, Long> depositMap = new HashMap<>();

        long totalSum = UserRecommendationServiceImpl.getTotalSum(withdrawal, depositMap);

        Assertions.assertEquals(0L, totalSum);
    }

    // Проверяем, что метод getTotalSum правильно рассчитывает общую сумму, когда не все списания имеют соответствующие депозиты
    @Test
    void testGetTotalSumWithPartialWithdrawalMatches() {
        List<PriceSum> withdrawal = new ArrayList<>();
        withdrawal.add(new PriceSum() {
            @Override
            public UUID getProductId() {
                return UUID.randomUUID();
            }

            @Override
            public Long getProductSum() {
                return 100L;
            }
        });
        withdrawal.add(new PriceSum() {
            @Override
            public UUID getProductId() {
                return UUID.randomUUID();
            }

            @Override
            public Long getProductSum() {
                return 50L;
            }
        });
        withdrawal.add(new PriceSum() {
            @Override
            public UUID getProductId() {
                return UUID.randomUUID();
            }

            @Override
            public Long getProductSum() {
                return 75L;
            }
        });

        Map<UUID, Long> depositMap = new HashMap<>();
        depositMap.put(withdrawal.get(0).getProductId(), 200L);
        depositMap.put(UUID.randomUUID(), 100L);
        depositMap.put(UUID.randomUUID(), 150L);

        long totalSum = UserRecommendationServiceImpl.getTotalSum(withdrawal, depositMap);

        Assertions.assertEquals(100L, totalSum);
    }

    // Проверяем, что метод isUserHaveCreditProduct возвращает true, когда пользователь имеет кредитный продукт
    @Test
    void UserHasCreditProduct() {
        Mockito.when(productRepository.findCreditProductsByUser(userId)).thenReturn(1);

        boolean result = userRecommendationService.isUserHaveCreditProduct(userId);

        Assertions.assertTrue(result);
    }

    // Проверяем, что метод isUserHaveCreditProduct возвращает false, когда пользователь не имеет кредитный продукт
    @Test
    void UserDoesNotHaveCreditProduct() {
        Mockito.when(productRepository.findCreditProductsByUser(userId)).thenReturn(0);

        boolean result = userRecommendationService.isUserHaveCreditProduct(userId);

        Assertions.assertFalse(result);
    }

    // Проверяем, что метод isWithdrawalSumOnDebitProductsMoreThan100000 возвращает true, когда сумма списаний по дебетовым продуктам больше 100 000
    @Test
    void SumIsMoreThan100000() {
        Mockito.when(productRepository.findSumOfDebitWithdrawalProductsByUser(userId)).thenReturn(150000L);

        boolean result = userRecommendationService.isWithdrawalSumOnDebitProductsMoreThan100000(userId);

        Assertions.assertTrue(result);
    }

    // Проверяем, что метод isWithdrawalSumOnDebitProductsMoreThan100000 возвращает false, когда сумма списаний по дебетовым продуктам меньше 100 000
    @Test
    void SumIsLessThan100000() {
        Mockito.when(productRepository.findSumOfDebitWithdrawalProductsByUser(userId)).thenReturn(50000L);

        boolean result = userRecommendationService.isWithdrawalSumOnDebitProductsMoreThan100000(userId);

        Assertions.assertFalse(result);
    }

    // Проверяем, что метод recommendInvestProduct возвращает true, когда все условия для рекомендации инвестиционного продукта выполнены.
    @Test
    void AllConditionsAreMet() {
        Mockito.when(userRecommendationService.isUserHaveDebitProduct(userId)).thenReturn(true);
        Mockito.when(userRecommendationService.isUserHaveInvestProduct(userId)).thenReturn(false);
        Mockito.when(userRecommendationService.isUserDepositInSavingProductPerOperationMoreThan1000(userId)).thenReturn(true);

        boolean result = userRecommendationService.recommendInvestProduct(userId);

        Assertions.assertTrue(result);
    }

    // Проверяем, что метод recommendInvestProduct возвращает false, когда не все условия для рекомендации инвестиционного продукта выполнены.
    @Test
    void NotAllConditionsAreMet() {
        Mockito.when(userRecommendationService.isUserHaveDebitProduct(userId)).thenReturn(false);
        Mockito.when(userRecommendationService.isUserHaveInvestProduct(userId)).thenReturn(true);
        Mockito.when(userRecommendationService.isUserDepositInSavingProductPerOperationMoreThan1000(userId)).thenReturn(true);

        boolean result = userRecommendationService.recommendInvestProduct(userId);

        Assertions.assertFalse(result);
    }

    // Проверяем, что метод который должен возвращать значение True, если пользователь не соответствует всем критериям
    @Test
    void WhenUserMeetsAllCriteria() {
        when(userRecommendationService.isUserHaveDebitProduct(userId)).thenReturn(true);
        when(userRecommendationService.isUserHaveMoreThanFiveOperationsInDebitOrSavingProductMoreThan10000(userId)).thenReturn(true);
        when(userRecommendationService.isUserHaveMoreDebitDepositAmountThanWithdrawalAmountOnSameProducts(userId)).thenReturn(true);

        boolean result = userRecommendationService.recommendSavingProduct(userId);

        assert result;
    }

    // Проверяем, что метод который должен возвращать значение false, если пользователь не соответствует всем критериям
    @Test
    void WhenUserDoesNotMeetAllCriteria() {
        when(userRecommendationService.isUserHaveDebitProduct(userId)).thenReturn(false);

        boolean result = userRecommendationService.recommendSavingProduct(userId);

        assert !result;
    }

    // Проверяем, что метод должен возвращать значение true, если пользователь соответствует всем критериям
    @Test
    void testRecommendCreditProductWhenUserMeetsAllCriteria() {
        when(userRecommendationService.isUserHaveCreditProduct(userId)).thenReturn(false);
        when(userRecommendationService.isUserHaveMoreDebitDepositAmountThanWithdrawalAmountOnSameProducts(userId)).thenReturn(true);
        when(userRecommendationService.isWithdrawalSumOnDebitProductsMoreThan100000(userId)).thenReturn(true);

        boolean result = userRecommendationService.recommendCreditProduct(userId);

        assert result;
    }

    // Проверяем, что метод должен возвращать значение False, если пользователь не соответствует всем критериям
    @Test
    void testRecommendCreditProductWhenUserDoesNotMeetAllCriteria() {
        when(userRecommendationService.isUserHaveCreditProduct(userId)).thenReturn(true);

        boolean result = userRecommendationService.recommendCreditProduct(userId);

        assert !result;
    }
}
