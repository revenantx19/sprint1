package com.skypro.sprint1.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.math.NumberUtils;

import static com.skypro.sprint1.model.Product.ProductType;


@Slf4j
@UtilityClass
public class RuleValidator {

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


    private static boolean validateProductType(String productType) {
        return EnumUtils.isValidEnum(ProductType.class, productType);
    }

    private static boolean validateNumberAndProductType(String number, String productType) {
        return NumberUtils.isCreatable(number) && validateProductType(productType);
    }

    private static boolean invalidRuleName(String rule) {
        log.error("Rule doesn't exist: {}", rule);
        return false;
    }



}
