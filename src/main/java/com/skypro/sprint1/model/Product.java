package com.skypro.sprint1.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Представляет собой сущность продукта.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String type;
    private String name;

    public Product(String type, String name) {
        this.type = type;
        this.name = name;
    }


}
