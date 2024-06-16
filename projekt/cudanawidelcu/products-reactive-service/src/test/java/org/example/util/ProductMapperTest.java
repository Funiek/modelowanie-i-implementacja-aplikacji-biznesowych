package org.example.util;

import org.example.dto.ProductDto;
import org.example.model.Product;
import org.example.util.ProductMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductMapperTest {
    @Test
    void convertProductToProductDto_ShouldReturnProductDto() {
        // Given
        Product product = Product.builder()
                .id(1L)
                .recipeId(2L)
                .name("Test Product")
                .measure("kg")
                .qty(5.0)
                .createdAt(LocalDateTime.now())
                .build();

        // When
        ProductDto productDto = ProductMapper.convertProductToProductDto(product);

        // Then
        assertThat(productDto).isNotNull();
        assertThat(productDto.getId()).isEqualTo(product.getId());
        assertThat(productDto.getRecipeId()).isEqualTo(product.getRecipeId());
        assertThat(productDto.getName()).isEqualTo(product.getName());
        assertThat(productDto.getMeasure()).isEqualTo(product.getMeasure());
        assertThat(productDto.getQty()).isEqualTo(product.getQty());
    }
    @Test
    void convertProductDtoToProduct_ShouldReturnProduct() {
        // Given
        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .recipeId(2L)
                .name("Test Product")
                .measure("kg")
                .qty(5.0)
                .build();

        // When
        Product product = ProductMapper.convertProductDtoToProduct(productDto);

        // Then
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(productDto.getId());
        assertThat(product.getRecipeId()).isEqualTo(productDto.getRecipeId());
        assertThat(product.getName()).isEqualTo(productDto.getName());
        assertThat(product.getMeasure()).isEqualTo(productDto.getMeasure());
        assertThat(product.getQty()).isEqualTo(productDto.getQty());
        assertThat(product.getCreatedAt()).isNotNull();
    }
}
