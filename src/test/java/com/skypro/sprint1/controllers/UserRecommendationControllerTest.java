package com.skypro.sprint1.controllers;

import com.skypro.sprint1.controller.UserRecommendationController;
import com.skypro.sprint1.pojo.UserRecommendation;
import com.skypro.sprint1.service.UserRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRecommendationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private UUID userId;

    @Mock
    private UserRecommendationService userRecommendationService;

    private UserRecommendationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new UserRecommendationController(userRecommendationService);
    }

    // Проверяем, что ответ имеет статус 200 и тело ответа содержит созданный объект userRecommendation
    @Test
    void testGetRecommendations_UserFound() {
        UserRecommendation expectedUserRecommendation = new UserRecommendation(userId, List.of());

        when(userRecommendationService.getRecommendations(userId)).thenReturn(expectedUserRecommendation);

        ResponseEntity<UserRecommendation> response = controller.getRecommendations(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedUserRecommendation, response.getBody());
        verify(userRecommendationService).getRecommendations(userId);
    }

    // Проверяем, что ответ имеет статус 404 и тело ответа содержит null
    @Test
    void testGetRecommendations_UserNotFound() {
        when(userRecommendationService.getRecommendations(userId)).thenThrow(ChangeSetPersister.NotFoundException.class);

        ResponseEntity<UserRecommendation> response = controller.getRecommendations(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}

