package com.skypro.sprint1.service;

import com.skypro.sprint1.pojo.UserRecommendation;

import java.util.UUID;

public interface UserRecommendationService {
    UserRecommendation getRecommendations(UUID userId);
    void clearRecommendationCache();
}
