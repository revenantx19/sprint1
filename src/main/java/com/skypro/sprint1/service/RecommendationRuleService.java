package com.skypro.sprint1.service;

import com.skypro.sprint1.model.RecommendationRule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleService {

    Optional<RecommendationRule> createRule(RecommendationRule rule);
    void deleteRule(UUID ruleId);
    Optional<RecommendationRule> getRule(UUID ruleId);
    List<RecommendationRule> getRules();
}
