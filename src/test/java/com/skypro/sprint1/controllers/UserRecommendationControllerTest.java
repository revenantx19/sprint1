package com.skypro.sprint1.controllers;

import com.skypro.sprint1.pojo.Recommendation;
import com.skypro.sprint1.pojo.UserRecommendation;
import com.skypro.sprint1.service.impl.UserRecommendationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRecommendationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @InjectMocks
    private UserRecommendationServiceImpl userRecommendationService;

    private UUID userId;

    @BeforeEach
    public void setup() {
        userId = UUID.randomUUID();
    }

    // Проверяем, что ответ имеет статус 200 и тело ответа содержит созданный объект userRecommendation
    @Test
    void getRecommendations_shouldReturnOkWithRecommendations() {
        UserRecommendation userRecommendation = new UserRecommendation(userId, List.of(new Recommendation(), new Recommendation()));

        when(userRecommendationService.getRecommendations(userId)).thenReturn(Optional.of(userRecommendation));

        ResponseEntity<UserRecommendation> response = restTemplate.getForEntity("/recommendation/" + userId, UserRecommendation.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userRecommendation);
    }

    // Проверяем, что ответ имеет статус 404 и тело ответа содержит null
    @Test
    void getRecommendations_shouldReturnNotFoundWhenRecommendationsNotFound() {
        when(userRecommendationService.getRecommendations(userId)).thenReturn(Optional.empty());

        ResponseEntity<UserRecommendation> response = restTemplate.getForEntity("/recommendation/" + userId, UserRecommendation.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }
}

