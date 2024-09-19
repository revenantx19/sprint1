package com.skypro.sprint1.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "rules")
public class RecommendationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String rule;

    private UUID productId;
    private String productName;
    private String productDescription;

    public RecommendationRule(String rule, UUID productId, String productName, String productDescription) {
        this.rule = rule;
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
    }
}
