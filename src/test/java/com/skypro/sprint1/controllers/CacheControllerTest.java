package com.skypro.sprint1.controllers;

import com.skypro.sprint1.controller.ManagementController;
import com.skypro.sprint1.service.UserRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

class CacheControllerTest {

    @Mock
    private UserRecommendationService userRecommendationService;

    @Mock
    private BuildProperties buildProperties;

    private ManagementController managementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        managementController = new ManagementController(userRecommendationService, buildProperties);
    }

    @Test
    void clearRecommendationCache_shouldClearCacheAndReturnOk() {
        ResponseEntity<String> response = managementController.clearRecommendationCache();

        verify(userRecommendationService).clearRecommendationCache();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Cache cleared");
    }
}
