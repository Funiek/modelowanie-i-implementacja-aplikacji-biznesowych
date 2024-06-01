package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private Long id;
    private Long recipeId;
    private String name;
    private String measure;
    private Double qty;
}
