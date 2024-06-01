package org.example.service;

import org.example.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Flux<Product> findAll();

    Flux<Product> findAllByRecipe(Long recipeId);

    Mono<Product> save(Product product);

    Mono<Void> deleteAllByRecipeId(Long recipeId);
}

