package com.skypro.sprint1.controller;

import com.skypro.sprint1.service.UserRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
public class CacheController {

    private final UserRecommendationService userRecommendationService;

    public CacheController(UserRecommendationService userRecommendationService) {
        this.userRecommendationService = userRecommendationService;
    }

    @GetMapping("/clear-caches")
    public ResponseEntity<String> clearRecommendationCache() {
        userRecommendationService.clearRecommendationCache();
        return ResponseEntity.ok("Cache cleared");
    }
}
