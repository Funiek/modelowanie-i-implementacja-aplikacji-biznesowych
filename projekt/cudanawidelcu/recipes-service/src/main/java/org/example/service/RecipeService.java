package org.example.service;

import org.example.exceptions.RecipeNotFoundException;
import org.example.model.Category;
import org.example.model.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RecipeService {
    Flux<Recipe> getRecipes();
    Mono<Recipe> getRecipe(Long id);
//    Recipe getRecipeByName(String name);
    Mono<Recipe> createRecipe(Recipe recipe);
    Mono<Recipe> updateRecipe(String name, Recipe recipe);
    Mono<Void> deleteRecipe(Long id);
    Mono<Recipe> rateRecipe(Long id, int vote) throws NullPointerException;
//    List<Recipe> getRecipesByCategory(Category category);
}
