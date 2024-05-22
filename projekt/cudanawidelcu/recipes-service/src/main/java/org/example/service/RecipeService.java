package org.example.service;

import org.example.model.Category;
import org.example.model.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getRecipes();
    Recipe getRecipe(Long id);
//    Recipe getRecipeByName(String name);
    Recipe createRecipe(Recipe recipe);
    Recipe updateRecipe(Long id, Recipe recipe);
    void deleteRecipe(Long id);
    Recipe rateRecipe(Long id, int vote) throws NullPointerException;

    List<Recipe> getRecipesByCategory(Category category);
}
