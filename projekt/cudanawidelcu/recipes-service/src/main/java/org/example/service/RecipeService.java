package org.example.service;

import org.example.exceptions.RecipeNotFoundException;
import org.example.model.Category;
import org.example.model.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getRecipes();
    Recipe getRecipeByName(String name);
    Recipe createRecipe(Recipe recipe);
    Recipe updateRecipe(String name, Recipe recipe);
    void deleteRecipeByName(String name);
    Recipe rateRecipe(String name, int vote) throws RecipeNotFoundException;
    List<Recipe> getRecipesByCategory(Category category);
}
