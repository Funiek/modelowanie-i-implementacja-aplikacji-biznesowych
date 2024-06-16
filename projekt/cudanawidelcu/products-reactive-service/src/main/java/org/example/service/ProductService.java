package org.example.service;

import org.example.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductService {
    Flux<Product> findAll();

    Flux<Product> findAllByRecipe(Long recipeId);

    Mono<Product> save(Product product);
    Flux<Product> saveAll(List<Product> products);

    Mono<Void> deleteAllByRecipeId(Long recipeId);
}

