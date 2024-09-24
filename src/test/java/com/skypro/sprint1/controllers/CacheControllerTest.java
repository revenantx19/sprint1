package com.skypro.sprint1.controllers;

import com.skypro.sprint1.controller.CacheController;
import com.skypro.sprint1.service.UserRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

class CacheControllerTest {

    @Mock
    private UserRecommendationService userRecommendationService;

    private CacheController cacheController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cacheController = new CacheController(userRecommendationService);
    }

    @Test
    void clearRecommendationCache_shouldClearCacheAndReturnOk() {
        ResponseEntity<String> response = cacheController.clearRecommendationCache();

        verify(userRecommendationService).clearRecommendationCache();
        assertThat(response.getStatusCode()).isEqualTo(ResponseEntity.ok());
        assertThat(response.getBody()).isEqualTo("Cache cleared");
    }
}
