package com.skypro.sprint1.service.impl;

import com.skypro.sprint1.model.RecommendationRule;
import com.skypro.sprint1.util.RuleValidator;
import com.skypro.sprint1.repository.RecommendationRuleRepository;
import com.skypro.sprint1.service.RecommendationRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RecommendationRuleServiceImpl implements RecommendationRuleService {

    private final RecommendationRuleRepository recommendationRuleRepository;
    private final CacheManager cacheManager;

    public RecommendationRuleServiceImpl(RecommendationRuleRepository recommendationRuleRepository, CacheManager cacheManager) {
        this.recommendationRuleRepository = recommendationRuleRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    public Optional<RecommendationRule> createRule(RecommendationRule rule) {

        boolean valid = RuleValidator.validate(rule.getRule());

        if (!valid) {
            log.warn("Rule is not saved");
            return Optional.empty();
        }

        log.info("Saving rule {}", rule.getRule());
        Objects.requireNonNull(cacheManager.getCache("userRecommendation")).clear();
        return Optional.of(recommendationRuleRepository.save(rule));
    }

    @Override
    public void deleteRule(UUID ruleId) {
        Objects.requireNonNull(cacheManager.getCache("userRecommendation")).clear();
        recommendationRuleRepository.deleteById(ruleId);
    }

    @Override
    public Optional<RecommendationRule> getRule(UUID ruleId) {
        return recommendationRuleRepository.findById(ruleId);
    }

    @Override
    public List<RecommendationRule> getRules() {
        return recommendationRuleRepository.findAll();
    }
}
