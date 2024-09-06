package com.skypro.sprint1.service.impl;

import com.skypro.sprint1.model.UserRecommendation;
import com.skypro.sprint1.service.UserRecommendationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserRecommendationServiceImpl implements UserRecommendationService {

    @Override
    public List<UserRecommendation> getRecommendations(UUID userId) {
        return List.of();
    }
}
