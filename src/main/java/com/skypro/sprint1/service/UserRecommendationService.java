package com.skypro.sprint1.service;

import com.skypro.sprint1.model.UserRecommendation;

import java.util.UUID;

public interface UserRecommendationService {
    UserRecommendation getRecommendations(UUID userId);
}
