package com.skypro.sprint1.model;

import java.util.UUID;

/**
 * Интерфейс для представления суммы по продукту.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
public interface PriceSum {

    /**
     * Возвращает идентификатор товара.
     *
     * @return Идентификатор товара в формате UUID.
     */
    UUID getProductId();

    /**
     * Возвращает сумму цены товара.
     *
     * @return Сумма цены товара в виде длинного целого числа.
     */
    Long getProductSum();
}
