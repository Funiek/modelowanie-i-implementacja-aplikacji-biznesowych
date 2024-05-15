package pl.cudanawidelcu.microservices.recipes.controller;


import org.springframework.web.bind.annotation.*;
import pl.cudanawidelcu.microservices.recipes.dto.RateRecipeDto;
import pl.cudanawidelcu.microservices.recipes.dto.RecipeDto;
import pl.cudanawidelcu.microservices.recipes.model.Recipe;
import pl.cudanawidelcu.microservices.recipes.service.RecipeService;
import pl.cudanawidelcu.microservices.recipes.util.RecipeMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    final
    RecipeService recipeService;
    protected Logger logger = Logger.getLogger(RecipeController.class.getName());

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

    @GetMapping("/name/{recipeName}")
    public RecipeDto getAll(@PathVariable("recipeName") String recipeName) {
        Recipe recipe = recipeService.getRecipeByName(recipeName);
        return RecipeMapper.convertRecipeToRecipeDto(recipe);
    }

    @PostMapping
    public RecipeDto add(@RequestBody RecipeDto recipeDto) {
        Recipe recipe = RecipeMapper.convertRecipeDtoToRecipe(recipeDto);
        Recipe createdRecipe = recipeService.createOrUpdateRecipe(recipe);
        return RecipeMapper.convertRecipeToRecipeDto(createdRecipe);
    }

    @PostMapping("/rate")
    public RecipeDto rate(@RequestBody RateRecipeDto rateRecipeDto) {
        Recipe recipe = recipeService.rateRecipe(rateRecipeDto.getName(), rateRecipeDto.getVote());
        return RecipeMapper.convertRecipeToRecipeDto(recipe);
    }
}
