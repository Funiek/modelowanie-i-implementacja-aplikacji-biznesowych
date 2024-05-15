package pl.cudanawidelcu.microservices.recipes.service;

import pl.cudanawidelcu.microservices.exceptions.RecipeNotFoundException;
import pl.cudanawidelcu.microservices.recipes.model.Category;
import pl.cudanawidelcu.microservices.recipes.model.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getRecipes();
    Recipe getRecipeByName(String name);
    Recipe createOrUpdateRecipe(Recipe recipe);
    void deleteRecipeByName(String name);
    Recipe rateRecipe(String name, int vote) throws RecipeNotFoundException;
    List<Recipe> getRecipesByCategory(Category category);
}
