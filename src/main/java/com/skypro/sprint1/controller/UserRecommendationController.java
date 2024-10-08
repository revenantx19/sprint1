package com.skypro.sprint1.controller;

import com.skypro.sprint1.pojo.UserRecommendation;
import com.skypro.sprint1.service.UserRecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Контроллер для обработки запросов, связанных с рекомендациями для пользователей.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */

@RestController
@Slf4j
@RequestMapping("/recommendation")
public class UserRecommendationController {

    private final UserRecommendationService userRecommendationService;

    public UserRecommendationController(UserRecommendationService userRecommendationService) {
        this.userRecommendationService = userRecommendationService;
    }

    /**
     * Получение рекомендаций для пользователя по его ID.
     *
     * @param userId ID пользователя.
     * @return Ответ с рекомендациями для пользователя, если они найдены.
     * Возвращает статус 404(Not Found), если рекомендации не найдены.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserRecommendation> getRecommendations(@PathVariable UUID userId) {
        UserRecommendation userRecommendation = userRecommendationService.getRecommendations(userId);
        if (userRecommendation.getRecommendations().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userRecommendation);
    }

}
