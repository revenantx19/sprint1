package com.skypro.sprint1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {

    private String productName;
    private UUID productId;
    private String productDescription;
}
