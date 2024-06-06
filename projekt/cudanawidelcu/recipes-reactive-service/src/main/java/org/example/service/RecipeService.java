package org.example.service;

import org.example.model.Category;
import org.example.model.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
    Flux<Recipe> findAll();
    Flux<Recipe> findAllByCategory(Category category);

    Mono<Recipe> save(Recipe recipe);

    Mono<Recipe> update(Long id, Recipe recipe);

    Mono<Void> deleteById(Long id);
}
