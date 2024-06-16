package org.example.util;

import org.example.dto.ProductDto;
import org.example.model.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for converting between Product and ProductDto.
 */
public class ProductMapper {

    /**
     * Converts Product entity to ProductDto.
     *
     * @param product the Product entity to convert
     * @return the corresponding ProductDto
     */
    public static ProductDto convertProductToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .recipeId(product.getRecipeId())
                .name(product.getName())
                .qty(product.getQty())
                .measure(product.getMeasure())
                .build();
    }

    /**
     * Converts ProductDto to Product entity.
     *
     * @param productDto the ProductDto to convert
     * @return the corresponding Product entity
     */
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