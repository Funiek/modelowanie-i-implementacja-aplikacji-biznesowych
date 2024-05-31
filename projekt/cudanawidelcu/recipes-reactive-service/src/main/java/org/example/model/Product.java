package org.example.model;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private Long id;
    private Long recipeId;
    private String name;
    private String measure;
    private Double qty;
}
