package com.skypro.sprint1.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private UUID userId;
    private UUID productId;
    private Long amount;

    public Transaction(TransactionType type, UUID userId, UUID productId, Long amount) {
        this.type = type;
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
    }

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL
    }
}
