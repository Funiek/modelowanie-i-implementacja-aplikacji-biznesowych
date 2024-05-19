package org.example.controller;


import org.example.dto.RateRecipeDto;
import org.example.dto.RecipeDto;
import org.example.exceptions.RecipeNotFoundException;
import org.example.model.Recipe;
import org.example.model.Vote;
import org.example.service.RecipeService;
import org.example.util.RecipeMapper;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/recipes")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class RecipeController {
    final
    RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public Flux<RecipeDto> getAll() {
        Flux<Recipe> recipes = recipeService.getRecipes();

        return recipes.map(RecipeMapper::convertRecipeToRecipeDto);
    }

//    @GetMapping("/{recipeName}")
//    public RecipeDto getByName(@PathVariable("recipeName") String recipeName) {
//        Recipe recipe = recipeService.getRecipeByName(recipeName);
//        return RecipeMapper.convertRecipeToRecipeDto(recipe);
//    }

    @GetMapping("/{recipeId}")
    public Mono<RecipeDto> get(@PathVariable("recipeId") Long recipeId) {
        return recipeService.getRecipe(recipeId).map(RecipeMapper::convertRecipeToRecipeDto);
    }
    @PostMapping
    public Mono<RecipeDto> create(@RequestBody RecipeDto recipeDto) {
        Recipe recipe = RecipeMapper.convertRecipeDtoToRecipe(recipeDto);
        return recipeService.createRecipe(recipe)
                .map(RecipeMapper::convertRecipeToRecipeDto);
    }

    @PutMapping("/{recipeName}")
    public Mono<RecipeDto> update(@PathVariable("recipeName") String recipeName, @RequestBody RecipeDto recipeDto) {
        Recipe recipe = RecipeMapper.convertRecipeDtoToRecipe(recipeDto);
        return recipeService.updateRecipe(recipeName, recipe).map(RecipeMapper::convertRecipeToRecipeDto);
    }

    @PostMapping("/rate")
    public Mono<RecipeDto> rate(@RequestBody RateRecipeDto rateRecipeDto) {
        return recipeService.rateRecipe((long) rateRecipeDto.getId(), rateRecipeDto.getVote())
                .map(RecipeMapper::convertRecipeToRecipeDto)
                .onErrorMap(NullPointerException.class, e -> new RecipeNotFoundException(rateRecipeDto.getName()));
    }
}
