package com.skypro.sprint1.service;

import com.skypro.sprint1.model.RecommendationRule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с правилами рекомендаций.
 * Предоставляет методы для создания, удаления, получения и поиска правил рекомендаций.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
public interface RecommendationRuleService {

    Optional<RecommendationRule> createRule(RecommendationRule rule);

    void deleteRule(UUID ruleId);

    Optional<RecommendationRule> getRule(UUID ruleId);

    List<RecommendationRule> getRules();
}