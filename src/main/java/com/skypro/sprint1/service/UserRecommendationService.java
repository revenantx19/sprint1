package com.skypro.sprint1.service;

import com.skypro.sprint1.pojo.UserRecommendation;

import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс для сервиса рекомендаций пользователей.
 * Предоставляет методы для получения рекомендаций для конкретного пользователя.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
public interface UserRecommendationService {

    Optional<UserRecommendation> getRecommendations(UUID userId);

}
