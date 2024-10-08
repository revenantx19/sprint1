package com.skypro.sprint1.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Представляет сущность операции с транзакциями.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String type;
    private UUID userId;
    private UUID productId;
    private Long amount;

    public Transaction(String type, UUID userId, UUID productId, Long amount) {
        this.type = type;
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
    }

}
