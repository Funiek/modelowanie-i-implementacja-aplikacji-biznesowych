package org.example.service;

import org.example.dto.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductService {
    Mono<List<ProductDto>> findAll();

    Mono<List<ProductDto>> findAllByRecipe(Long recipeId);

    Mono<ProductDto> save(ProductDto productDto);

    Mono<Void> deleteAllByRecipeId(Long recipeId);
}

