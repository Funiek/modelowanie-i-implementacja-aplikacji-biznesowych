package org.example.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .recipeId(2L)
                .name("Test Product")
                .measure("kg")
                .qty(5.0)
                .build();
    }
    @Test
    void findAll_shouldReturnProducts() {
        // When
        when(productRepository.findAll()).thenReturn(Flux.just(product));

        Flux<Product> productFlux = productService.findAll();

        StepVerifier.create(productFlux)
                .expectNext(product)
                .verifyComplete();

        // Then
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findAllByRecipe_shouldReturnProductsByRecipeId() {
        // Given
        Long recipeId = 2L;

        // When
        when(productRepository.findAllByRecipeId(recipeId)).thenReturn(Flux.just(product));

        Flux<Product> productFlux = productService.findAllByRecipe(recipeId);

        StepVerifier.create(productFlux)
                .expectNext(product)
                .verifyComplete();

        // Then
        verify(productRepository, times(1)).findAllByRecipeId(recipeId);
    }

    @Test
    void save_shouldSaveProductsInRepo() {
        // When
        when(productRepository.save(product)).thenReturn(Mono.just(product));

        Mono<Product> savedProduct = productService.save(product);

        StepVerifier.create(savedProduct)
                .expectNext(product)
                .verifyComplete();

        // Then
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void saveAll_shouldSaveProductsInRepo() {
        // Given
        List<Product> products = Arrays.asList(product);

        // When
        when(productRepository.saveAll(products)).thenReturn(Flux.fromIterable(products));

        Flux<Product> savedProducts = productService.saveAll(products);

        StepVerifier.create(savedProducts)
                .expectNext(product)
                .verifyComplete();

        // Then
        verify(productRepository, times(1)).saveAll(products);
    }

    @Test
    void deleteAllByRecipeId_shouldDeleteProductsByRecipeIdInRepo() {
        // Given
        Long recipeId = 2L;

        // When
        when(productRepository.deleteAllByRecipeId(recipeId)).thenReturn(Mono.empty());

        Mono<Void> result = productService.deleteAllByRecipeId(recipeId);

        StepVerifier.create(result)
                .verifyComplete();

        // Then
        verify(productRepository, times(1)).deleteAllByRecipeId(recipeId);
    }
}