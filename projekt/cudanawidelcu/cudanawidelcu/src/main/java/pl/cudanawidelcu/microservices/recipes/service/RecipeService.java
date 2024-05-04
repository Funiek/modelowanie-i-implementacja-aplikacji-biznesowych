package pl.cudanawidelcu.microservices.recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.cudanawidelcu.microservices.exceptions.RecipeNotFoundException;
import pl.cudanawidelcu.microservices.recipes.model.Category;
import pl.cudanawidelcu.microservices.recipes.model.Recipe;
import pl.cudanawidelcu.microservices.recipes.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

public interface RecipeService {
    List<Recipe> getRecipes();
//
//        String getRecipesToString();
//
//        Recipe getRecipe(int id) throws RecipeNotFoundException;
//
//        Recipe getRecipeByName(String name);
//
//        void addRecipe(Recipe recipe);
//
//        void modifyRecipe(Recipe recipe);
//
//        void deleteRecipe(int id);
//
//        void deleteRecipeByName(String name);
//
//        void rateRecipe(int id, int vote) throws RecipeNotFoundException;
//
//        void rateRecipeByName(String name, int vote);
//
//        List<Recipe> getRecipesByCategory(Category category);

}
