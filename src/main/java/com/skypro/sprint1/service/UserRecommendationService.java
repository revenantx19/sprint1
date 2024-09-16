package com.skypro.sprint1.service;

import com.skypro.sprint1.pojo.UserRecommendation;

import java.util.Optional;
import java.util.UUID;

public interface UserRecommendationService {
    UserRecommendation getRecommendations(UUID userId);
    Optional<UserRecommendation> getRecommendationsByRules(UUID userId);
}
