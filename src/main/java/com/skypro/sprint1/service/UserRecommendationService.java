package com.skypro.sprint1.service;

import com.skypro.sprint1.model.UserRecommendation;

import java.util.List;
import java.util.UUID;

public interface UserRecommendationService {
    List<UserRecommendation> getRecommendations(UUID userId);
}
