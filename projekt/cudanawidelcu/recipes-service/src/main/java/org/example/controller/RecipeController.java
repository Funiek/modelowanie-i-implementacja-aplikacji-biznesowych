package org.example.controller;


import org.example.dto.RateRecipeDto;
import org.example.dto.RecipeDto;
import org.example.model.Recipe;
import org.example.service.RecipeService;
import org.example.util.RecipeMapper;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{recipeName}")
    public RecipeDto getByName(@PathVariable("recipeName") String recipeName) {
        Recipe recipe = recipeService.getRecipeByName(recipeName);
        return RecipeMapper.convertRecipeToRecipeDto(recipe);
    }

    @PostMapping
    public RecipeDto create(@RequestBody RecipeDto recipeDto) {
        Recipe recipe = RecipeMapper.convertRecipeDtoToRecipe(recipeDto);
        Recipe createdRecipe = recipeService.createRecipe(recipe);
        return RecipeMapper.convertRecipeToRecipeDto(createdRecipe);
    }

    @PutMapping("/{recipeName}")
    public RecipeDto update(@PathVariable("recipeName") String recipeName, @RequestBody RecipeDto recipeDto) {
        Recipe recipe = RecipeMapper.convertRecipeDtoToRecipe(recipeDto);
        Recipe createdRecipe = recipeService.updateRecipe(recipeName, recipe);
        return RecipeMapper.convertRecipeToRecipeDto(createdRecipe);
    }

    @PostMapping("/rate")
    public RecipeDto rate(@RequestBody RateRecipeDto rateRecipeDto) {
        Recipe recipe = recipeService.rateRecipe(rateRecipeDto.getName(), rateRecipeDto.getVote());
        return RecipeMapper.convertRecipeToRecipeDto(recipe);
    }
}
