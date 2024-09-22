package com.skypro.sprint1.repository;

import com.skypro.sprint1.model.Product;
import com.skypro.sprint1.model.Transaction;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/*
@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @MockBean
    private ProductRepository productRepository;

    //Тест проверяет метод findAmountOfDebitProductsOfUser, метод должен возвращать количество продуктов с типом DEBIT,
    //которые использует пользователь с заданным ID.

    @Test
    void findAmountOfDebitProductsOfUser() {
        //Создание случайного UUID для пользователя
        UUID userId = UUID.randomUUID();
        when(productRepository.findAmountOfDebitProductsOfUser(userId)).thenReturn(2);

        //Вызов метода findAmountOfDebitProductsOfUser и сохранение результата в переменной result
        Integer result = productRepository.findAmountOfDebitProductsOfUser(userId);

        //Проверка того, что у пользователя как минимум есть один префикс DEBIT
        assertTrue(result >= 1);

        //Проверка сответствия между result и ожидемым значением
        assertEquals(2, result);
    }

    //Тест проверяет метод findAmountOfInvestProductsOfUser, метод должен возвращать количество продуктов с типом INVEST,
    //которые использует пользователь с заданным ID.

    @Test
    void findAmountOfInvestProductsOfUser() {
        //Создание случайного UUID для пользователя
        UUID userId = UUID.randomUUID();
        when(productRepository.findAmountOfInvestProductsOfUser(userId)).thenReturn(3);

        //Вызов метода findAmountOfInvestProductsOfUser и сохранение результата в переменной result
        Integer result = productRepository.findAmountOfInvestProductsOfUser(userId);

        //Проверка соответствия между result и ожидаемым значением
        assertEquals(3, result);
    }

    //Тест проверяет работу метода findAmountOfInvestedSavingProductsOfUser,
    //когда пользователь пополнял продукты с префиксом SAVING на сумму 1000 рублей в любом месяце

    @Test
    void findAmountOfInvestedSavingProductsOfUser() {

        //Создание случайного UUID для пользователя
        UUID userId = UUID.randomUUID();

        //Проверка того, что пользователь пополнял любой из продуктов с префиксом SAVING на сумму до 1000 рублей
        when(productRepository.findAmountOfInvestedSavingProductsOfUser(userId)).thenReturn(5);
        assertTrue(productRepository.findAmountOfInvestedSavingProductsOfUser(userId) >= 1);

        //Вызов метода findAmountOfInvestedSavingProductsOfUser и сохранение результата
        Integer result = productRepository.findAmountOfInvestedSavingProductsOfUser(userId);

        //Проверка соответствия между result и ожидаемым значением
        assertEquals(5, result);
    }

    //Тест проверяет работу метода testFindAmountOfInvestedDebitProductsOrSavingProductsMoreThan10000, когда пользователь совершает
    //минимум 5 операций пополнения на любой продукт с префиксом DEBIT или SAVING более чем на 10 000 рублей за одну операцию

    @Test
    void testFindAmountOfInvestedDebitProductsOrSavingProductsMoreThan10000() {

        //Создание случайного UUID для пользователя
        UUID userId = UUID.randomUUID();

        //Создаем сущность transactions
        List<Transaction> transactions = new ArrayList<>();

        //С помощью цикла добаваляем 5 записей
        for (int i = 0; i < 5; i++) {
            Transaction transaction = new Transaction();
            transaction.setUserId(userId);
            transaction.setType("DEPOSIT");
            transaction.setAmount(15000);
            transaction.setProductId(UUID.randomUUID());

            Product product = new Product();
            product.setId(transaction.getProductId());
            product.setType("DEBIT");

            transactions.add(transaction);
        }

        when(entityManager.createNativeQuery(anyString()).getResultList()).thenReturn(transactions);

        Integer result = productRepository.findAmountOfInvestedDebitProductsOrSavingProductsMoreThan10000(userId);

        assertEquals(5, result.intValue());
    }

    //Тест проверяет, что метод findDebitDepositSumByProduct правильно находит сумму пополнений по каждому продукту

    @Test
    void findDebitDepositSumByProduct() {

        //Создание случайного UUID для пользователя
        UUID userId = UUID.randomUUID();

        //Создаем предполагаемую (ожидаемую) структуру результатов
        Map<UUID, Integer> expectedDepositSum = new HashMap<>();
        expectedDepositSum.put(UUID.randomUUID(), 15000);

        //Запрос на получение суммы депозитов для продукта связанных с пользовательским ID
        Map<UUID, Integer> depositSum = productRepository.findDebitDepositSumByProduct(userId);

        //Сравнение результатов
        assertEquals(expectedDepositSum, depositSum);
    }

    //Тест проверяет, что метод findDebitDepositSumByProduct правильно находит сумму трат по каждому продукту

    @Test
    void findDebitWithdrawalSumByProduct() {

        //Создание случайного UUID для пользователя
        UUID userId = UUID.randomUUID();

        //Создаем предполагаемую (ожидаемую) структуру результатов
        Map<UUID, Integer> expectedWithdrawalSum = new HashMap<>();
        expectedWithdrawalSum.put(UUID.randomUUID(), 0);

        //Запрос на получение суммы депозитов для продукта связанных с пользовательским ID
        Map<UUID, Integer> withdrawalSum = productRepository.findDebitWithdrawalSumByProduct(userId);

        //Сравнение результатов
        assertEquals(expectedWithdrawalSum, withdrawalSum);
    }
}
*/