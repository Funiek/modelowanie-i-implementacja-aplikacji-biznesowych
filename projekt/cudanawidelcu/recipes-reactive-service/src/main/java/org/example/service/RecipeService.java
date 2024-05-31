package org.example.service;

import org.example.model.Recipe;
import reactor.core.publisher.Flux;

public interface RecipeService {
    Flux<Recipe> getRecipes();
}
