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


/**
 * Тестовый класс для {@link ManagementController}.
 *
 * @author Vladimir Kuznetsov
 * @version 1.0
 */
class CacheControllerTest {

    @Mock
    private UserRecommendationService userRecommendationService;

    @Mock
    private BuildProperties buildProperties;

    private ManagementController managementController;

    /**
     * Инициализация тестовой среды перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        managementController = new ManagementController(userRecommendationService, buildProperties);
    }

    /**
     * Тест метода {@link ManagementController#clearRecommendationCache()}.
     * Проверяет, что метод очищает кэш рекомендаций и возвращает HTTP-ответ с кодом 200 (OK).
     */
    @Test
    void clearRecommendationCache_shouldClearCacheAndReturnOk() {
        ResponseEntity<String> response = managementController.clearRecommendationCache();

        verify(userRecommendationService).clearRecommendationCache();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Cache cleared");
    }
}