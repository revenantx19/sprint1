package com.skypro.sprint1.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.skypro.sprint1.model.RecommendationRule;
import com.skypro.sprint1.repository.RecommendationRuleRepository;
import com.skypro.sprint1.service.impl.RecommendationRuleServiceImpl;
import com.skypro.sprint1.util.RuleValidator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для сервиса RecommendationRuleServiceImpl.
 *
 * @author Vladimir Kuznetsov
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class RecommendationRuleServiceImplTest {

    @Mock
    private RecommendationRuleRepository recommendationRuleRepository;
    private Cache userRecommendationCache;

    @InjectMocks
    private RecommendationRuleServiceImpl recommendationRuleService;

    private UUID ruleId;
    private RecommendationRule rule;

    /**
     * Настройка перед каждым тестом.
     */
    @BeforeEach
    public void setUp() {
        // Здесь можно вставить любую настройку перед тестом
        ruleId = UUID.randomUUID();
        rule = new RecommendationRule();
        rule.setId(ruleId);
        rule.setRule("sampleRule");
    }

    /**
     * Тест shouldReturnRuleWhenExists: Проверяет, что метод getRule возвращает правило, если оно существует в репозитории.
     */
    @Test
    public void shouldReturnRuleWhenExists() {
        when(recommendationRuleRepository.findById(ruleId)).thenReturn(Optional.of(rule));

        Optional<RecommendationRule> result = recommendationRuleService.getRule(ruleId);

        assertTrue(result.isPresent());
        assertEquals(rule, result.get());
        verify(recommendationRuleRepository, times(1)).findById(ruleId);
    }

    /**
     * Тест shouldReturnEmptyWhenRuleDoesNotExist: Проверяет, что метод getRule возвращает пустое значение, если правило не найдено.
     */
    @Test
    public void shouldReturnEmptyWhenRuleDoesNotExist() {
        when(recommendationRuleRepository.findById(ruleId)).thenReturn(Optional.empty());

        Optional<RecommendationRule> result = recommendationRuleService.getRule(ruleId);

        assertFalse(result.isPresent());
        verify(recommendationRuleRepository, times(1)).findById(ruleId);
    }

    /**
     * Тест shouldReturnAllRules: Проверяет, что метод getRules возвращает все правила, находящиеся в репозитории.
     */
    @Test
    public void shouldReturnAllRules() {
        RecommendationRule rule1 = new RecommendationRule();
        rule1.setId(UUID.randomUUID());
        rule1.setRule("rule1");

        RecommendationRule rule2 = new RecommendationRule();
        rule2.setId(UUID.randomUUID());
        rule2.setRule("rule2");

        when(recommendationRuleRepository.findAll()).thenReturn(Arrays.asList(rule1, rule2));

        List<RecommendationRule> result = recommendationRuleService.getRules();

        assertEquals(2, result.size());
        assertTrue(result.contains(rule1));
        assertTrue(result.contains(rule2));
        verify(recommendationRuleRepository, times(1)).findAll();
    }
}