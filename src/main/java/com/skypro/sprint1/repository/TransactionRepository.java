package com.skypro.sprint1.repository;

import com.skypro.sprint1.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с операционными транзакциями.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {


}
