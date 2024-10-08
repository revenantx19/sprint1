package com.skypro.sprint1.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Представляет рекомендацию продукта.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {

    private String productName;
    private UUID productId;
    private String productDescription;
}
