package com.skypro.sprint1.util;

import com.skypro.sprint1.pojo.Recommendation;
import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * Утилита для получения рекомендаций по продуктам.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@UtilityClass
public class RecommendationProductUtil {

    public Recommendation getInvestProduct() {
        return new Recommendation("Invest 500",
                UUID.randomUUID(),
                RecommendationMessageUtil.INVEST_MESSAGE);
    }

    public static Recommendation getSavingProduct() {
        return new Recommendation("Top Saving",
                UUID.randomUUID(),
                RecommendationMessageUtil.SAVING_MESSAGE);
    }

    public static Recommendation getCreditProduct() {
        return new Recommendation("Простой кредит",
                UUID.randomUUID(),
                RecommendationMessageUtil.CREDIT_MESSAGE);
    }
}
