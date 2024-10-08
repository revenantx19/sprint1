package com.skypro.sprint1.controller;

import com.skypro.sprint1.model.RecommendationRule;
import com.skypro.sprint1.service.RecommendationRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class RecommendationRuleController {

    private final RecommendationRuleService recommendationRuleService;

    public RecommendationRuleController(RecommendationRuleService recommendationRuleService) {
        this.recommendationRuleService = recommendationRuleService;
    }

    @PostMapping
    public ResponseEntity<RecommendationRule> createRule(@RequestBody RecommendationRule recommendationRule) {
        Optional<RecommendationRule> rule = recommendationRuleService.createRule(recommendationRule);
        return rule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{ruleId}")
    public void deleteRule(@PathVariable UUID ruleId) {
        recommendationRuleService.deleteRule(ruleId);
    }

    @GetMapping("/{ruleId}")
    public ResponseEntity<RecommendationRule> getRule(@PathVariable UUID ruleId) {
        Optional<RecommendationRule> recommendationRule = recommendationRuleService.getRule(ruleId);
        return recommendationRule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<RecommendationRule> getRules() {
        return recommendationRuleService.getRules();
    }


}
