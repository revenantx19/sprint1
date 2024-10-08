package com.skypro.sprint1.controllers;

import com.skypro.sprint1.controller.RecommendationRuleController;
import com.skypro.sprint1.model.RecommendationRule;
import com.skypro.sprint1.service.RecommendationRuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для {@link RecommendationRuleController}.
 *
 * @author Vladimir Kuznetsov
 * @version 1.0
 */
class RecommendationRuleControllerTest {

    @Mock
    private RecommendationRuleService recommendationRuleService;

    private RecommendationRuleController controller;

    private RecommendationRule rule1;
    private RecommendationRule rule2;
    private UUID ruleId1;

    /**
     * Настройка перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new RecommendationRuleController(recommendationRuleService);

        ruleId1 = UUID.randomUUID();
        rule1 = new RecommendationRule("rule1", UUID.randomUUID(), "productName1", "productDescription1");
        rule2 = new RecommendationRule("rule2", UUID.randomUUID(), "productName2", "productDescription2");
    }

    /**
     * Тест метода {@link RecommendationRuleController#createRule(RecommendationRule)}, проверяющий возврат ответа 200 при успешном создании правила.
     */
    @Test
    void shouldReturnOkWithCreatedRule() {
        when(recommendationRuleService.createRule(rule1)).thenReturn(Optional.of(rule1));

        ResponseEntity<RecommendationRule> response = controller.createRule(rule1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(rule1);
        verify(recommendationRuleService).createRule(rule1);
    }

    /**
     * Тест метода {@link RecommendationRuleController#createRule(RecommendationRule)}, проверяющий возврат ответа 200 при успешном создании правила.
     */
    @Test
    void shouldReturnNotFoundWhenRuleNotCreated() {
        when(recommendationRuleService.createRule(rule1)).thenReturn(Optional.empty());

        ResponseEntity<RecommendationRule> response = controller.createRule(rule1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(recommendationRuleService).createRule(rule1);
    }

    /**
     * Тест метода {@link RecommendationRuleController#deleteRule(UUID)}, проверяющий вызов сервиса удаления правила.
     */
    @Test
    void deleteRuleTest() {
        controller.deleteRule(ruleId1);

        verify(recommendationRuleService).deleteRule(ruleId1);
    }

    /**
     * Тест метода {@link RecommendationRuleController#getRule(UUID)}, проверяющий возврат ответа 200 при успешном получении правила.
     */
    @Test
    void shouldReturnOkWithFoundRule() {
        when(recommendationRuleService.getRule(ruleId1)).thenReturn(Optional.of(rule1));

        ResponseEntity<RecommendationRule> response = controller.getRule(ruleId1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(rule1);
        verify(recommendationRuleService).getRule(ruleId1);
    }

    /**
     * Тест метода {@link RecommendationRuleController#getRule(UUID)}, проверяющий возврат ответа 404 при неуспешном получении правила.
     */
    @Test
    void shouldReturnNotFoundWhenRuleNotFound() {
        when(recommendationRuleService.getRule(ruleId1)).thenReturn(Optional.empty());

        ResponseEntity<RecommendationRule> response = controller.getRule(ruleId1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(recommendationRuleService).getRule(ruleId1);
    }

    /**
     * Тест получения всех рекомендационных правил.
     */
    @Test
    void shouldReturnAllRules() {
        when(recommendationRuleService.getRules()).thenReturn(Arrays.asList(rule1, rule2));

        List<RecommendationRule> rules = controller.getRules();

        assertThat(rules).hasSize(2);
        assertThat(rules).containsExactly(rule1, rule2);
        verify(recommendationRuleService).getRules();
    }
}