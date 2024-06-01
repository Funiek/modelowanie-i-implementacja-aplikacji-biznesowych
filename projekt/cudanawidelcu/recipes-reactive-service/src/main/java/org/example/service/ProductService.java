package org.example.service;

import org.example.dto.ProductDto;
import org.example.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Flux<ProductDto> findAll();

    Flux<ProductDto> findAllByRecipe(Long recipeId);

    Mono<ProductDto> save(ProductDto productDto);

    Mono<Void> deleteAllByRecipeId(Long recipeId);
}

