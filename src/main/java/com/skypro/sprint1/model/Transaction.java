package com.skypro.sprint1.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String type;
    private UUID userId;
    private UUID productId;
    private Integer amount;

    public Transaction(String type, UUID userId, UUID productId, Integer amount) {
        this.type = type;
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
    }
}
