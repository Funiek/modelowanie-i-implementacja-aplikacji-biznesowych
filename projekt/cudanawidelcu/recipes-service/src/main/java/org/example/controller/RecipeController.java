package org.example.controller;


import org.example.dto.RateRecipeDto;
import org.example.dto.RecipeDto;
import org.example.exceptions.RecipeNotFoundException;
import org.example.model.Category;
import org.example.model.Recipe;
import org.example.model.Vote;
import org.example.service.RecipeService;
import org.example.util.RecipeMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/recipes")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
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

//    @GetMapping("/{recipeName}")
//    public RecipeDto getByName(@PathVariable("recipeName") String recipeName) {
//        Recipe recipe = recipeService.getRecipeByName(recipeName);
//        return RecipeMapper.convertRecipeToRecipeDto(recipe);
//    }

    @GetMapping("/{recipeId}")
    public RecipeDto get(@PathVariable("recipeId") Long recipeId) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        return RecipeMapper.convertRecipeToRecipeDto(recipe);
    }

    @GetMapping("/category/{categoryName}")
    public List<RecipeDto> getByCategory(@PathVariable("categoryName") String categoryName) {
        Category category = Category.valueOf(categoryName.toUpperCase());

        List<Recipe> recipes = recipeService.getRecipesByCategory(category);
        return recipes.stream()
                .map(RecipeMapper::convertRecipeToRecipeDto)
                .collect(Collectors.toList());
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
        Recipe recipe;

        try {
            recipe = recipeService.rateRecipe(rateRecipeDto.getId(), rateRecipeDto.getVote());
        } catch (NullPointerException e) {
            throw new RecipeNotFoundException(rateRecipeDto.getName());
        }

        return RecipeMapper.convertRecipeToRecipeDto(recipe);
    }
}
