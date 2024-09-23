package com.skypro.sprint1.controller;

import com.skypro.sprint1.model.RecommendationRule;
import com.skypro.sprint1.service.RecommendationRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Класс для работы с рекомендательными правилами.
 * Предоставляет API для создания, удаления, получения по ID и получения списка правил.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */

@RestController
@RequestMapping("/rule")
public class RecommendationRuleController {

    private final RecommendationRuleService recommendationRuleService;

    public RecommendationRuleController(RecommendationRuleService recommendationRuleService) {
        this.recommendationRuleService = recommendationRuleService;
    }

    /**
     * Создает новое рекомендательное правило.
     *
     * @param recommendationRule Правило, которое необходимо создать.
     * @return Созданное правило, если оно успешно создано.
     * Возвращает 404 Not Found, если создание правила не удалось.
     */
    @PostMapping
    public ResponseEntity<RecommendationRule> createRule(@RequestBody RecommendationRule recommendationRule) {
        Optional<RecommendationRule> rule = recommendationRuleService.createRule(recommendationRule);
        return rule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Удаляет рекомендательное правило по его ID.
     *
     * @param ruleId ID удаляемого правила.
     */
    @DeleteMapping("/{ruleId}")
    public void deleteRule(@PathVariable UUID ruleId) {
        recommendationRuleService.deleteRule(ruleId);
    }

    /**
     * Возвращает рекомендательное правило по его ID.
     *
     * @param ruleId ID правила, которое необходимо получить.
     * @return Найденное правило, если оно существует. Возвращает 404 * Not Found, если правило не найдено.
     */
    @GetMapping("/{ruleId}")
    public ResponseEntity<RecommendationRule> getRule(@PathVariable UUID ruleId) {
        Optional<RecommendationRule> recommendationRule = recommendationRuleService.getRule(ruleId);
        return recommendationRule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Возвращает список всех рекомендательных правил.
     *
     * @return Список всех рекомендательных правил.
     */
    @GetMapping
    public List<RecommendationRule> getRules() {
        return recommendationRuleService.getRules();
    }
}