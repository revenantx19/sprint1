package com.skypro.sprint1.service.impl;

import com.skypro.sprint1.model.RecommendationRule;
import com.skypro.sprint1.util.RuleValidator;
import com.skypro.sprint1.repository.RecommendationRuleRepository;
import com.skypro.sprint1.service.RecommendationRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RecommendationRuleServiceImpl implements RecommendationRuleService {

    private final RecommendationRuleRepository recommendationRuleRepository;

    public RecommendationRuleServiceImpl(RecommendationRuleRepository recommendationRuleRepository) {
        this.recommendationRuleRepository = recommendationRuleRepository;
    }

    @Override
    public Optional<RecommendationRule> createRule(RecommendationRule rule) {

        boolean valid = RuleValidator.validate(rule.getRule());

        if (!valid) {
            log.warn("Rule is not saved");
            return Optional.empty();
        }

        log.info("Saving rule {}", rule.getRule());
        return Optional.of(recommendationRuleRepository.save(rule));
    }

    @Override
    public void deleteRule(UUID ruleId) {
        recommendationRuleRepository.deleteById(ruleId);
    }

    @Override
    @Cacheable(value = "rule", key = "#ruleId")
    public Optional<RecommendationRule> getRule(UUID ruleId) {
        return recommendationRuleRepository.findById(ruleId);
    }

    @Override
    public List<RecommendationRule> getRules() {
        return recommendationRuleRepository.findAll();
    }
}
