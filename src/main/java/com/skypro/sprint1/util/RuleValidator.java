package com.skypro.sprint1.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Класс для валидации правил.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Slf4j
@UtilityClass
public class RuleValidator {

    /**
     * Проверяет валидность правила.
     * Правила должны быть разделены символом '%'.
     * Каждое правило должно быть в формате "имя_правила:аргумент1:аргумент2".
     *
     * @param rule Правило для проверки.
     * @return {@code true}, если правило валидно, иначе {@code false}.
     */
    public static boolean validate(String rule) {
        String[] rules = rule.split("%");

        if (rules.length < 1) {
            log.warn("No rules");
            return false;
        }

        if (rules.length > 10) {
            log.warn("Too many rules. Max 10 rules allowed.");
            return false;
        }

        for (String r : rules) {
            String[] ruleArgs= r.split(":");

            try {
                boolean valid = switch (ruleArgs[0]) {
                    case "userOf", "notUserOf", "topupGTSpend", "activeUserOf" -> validateProductType(ruleArgs[1]);
                    case "topup", "topupSGT", "spendSGT" -> validateNumberAndProductType(ruleArgs[1], ruleArgs[2]);
                    default -> invalidRuleName(ruleArgs[0]);
                };

                if (!valid) {
                    log.error("Rule is not valid: {}", rule);
                    return false;
                }

            } catch (IndexOutOfBoundsException e) {
                log.error("Invalid amount of arguments");
                return false;
            }
        }

        return true;
    }

    /**
     * Проверяет валидность типа продукта.
     *
     * @param productType Тип продукта.
     * @return {@code true}, если тип продукта валиден, иначе {@code false}.
     */
    private static boolean validateProductType(String productType) {
        return productType.equals("DEBIT") || productType.equals("CREDIT") || productType.equals("INVEST") || productType.equals("SAVING");
    }

    /**
     * Проверяет валидность номера и типа продукта.
     *
     * @param number      Номер.
     * @param productType Тип продукта.
     * @return {@code true}, если номер и тип продукта валидны, иначе {@code false}.
     */
    private static boolean validateNumberAndProductType(String number, String productType) {
        return NumberUtils.isCreatable(number) && validateProductType(productType);
    }

    /**
     * Проверяет валидность имени правила.
     *
     * @param rule имя правила.
     * @return {@code false}.
     */
    private static boolean invalidRuleName(String rule) {
        log.error("Rule doesn't exist: {}", rule);
        return false;
    }



}
