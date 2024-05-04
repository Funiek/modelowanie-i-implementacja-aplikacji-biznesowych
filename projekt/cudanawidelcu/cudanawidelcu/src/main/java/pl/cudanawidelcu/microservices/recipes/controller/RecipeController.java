package pl.cudanawidelcu.microservices.recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cudanawidelcu.microservices.recipes.dto.RecipeDto;
import pl.cudanawidelcu.microservices.recipes.model.Recipe;
import pl.cudanawidelcu.microservices.recipes.service.RecipeService;
import pl.cudanawidelcu.microservices.recipes.util.RecipeMapper;
import pl.cudanawidelcu.microservices.recipes.util.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    final
    RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeDto> getAll() {
        List<Recipe> recipes = recipeService.getRecipes();

        return recipes.stream()
                .map(RecipeMapper::convertRecipeToRecipeDto)
                .collect(Collectors.toList());
    }
}
