package com.skypro.sprint1.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Представляет рекомендации для определенного пользователя.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecommendation {

    private UUID userId;
    private List<Recommendation> recommendations;
}