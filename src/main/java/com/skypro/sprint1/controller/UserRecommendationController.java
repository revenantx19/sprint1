package com.skypro.sprint1.controller;

import com.skypro.sprint1.model.UserRecommendation;
import com.skypro.sprint1.service.UserRecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class UserRecommendationController {

    private final UserRecommendationService userRecommendationService;

    public UserRecommendationController(UserRecommendationService userRecommendationService) {
        this.userRecommendationService = userRecommendationService;
    }

    @GetMapping("/{user_id}")
    public List<UserRecommendation> getRecommendations(@PathVariable(name = "user_id") UUID userId) {
        return userRecommendationService.getRecommendations(userId);
    }
}
