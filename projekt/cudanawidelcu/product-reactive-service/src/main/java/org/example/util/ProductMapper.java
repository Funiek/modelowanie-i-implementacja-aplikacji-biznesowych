package org.example.util;

import org.example.dto.ProductDto;
import org.example.model.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    public static ProductDto convertProductToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .recipeId(product.getRecipeId())
                .name(product.getName())
                .qty(product.getQty())
                .measure(product.getMeasure())
                .build();
    }

    public static Product convertProductDtoToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .recipeId(productDto.getRecipeId())
                .name(productDto.getName())
                .qty(productDto.getQty())
                .measure(productDto.getMeasure())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
