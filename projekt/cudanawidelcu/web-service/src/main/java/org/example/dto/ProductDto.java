package org.example.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String measure;
    private Double qty;
}
