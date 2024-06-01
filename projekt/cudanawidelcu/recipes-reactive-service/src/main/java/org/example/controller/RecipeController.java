package org.example.controller;

import org.example.dto.RecipeDto;
import org.example.model.Recipe;
import org.example.service.RecipeService;
import org.example.util.RecipeMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;

@RestController
@EnableWebFlux
@RequestMapping("/api/v1/recipes")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public Flux<RecipeDto> findAll() {
        Flux<Recipe> recipes = recipeService.getRecipes();

        return recipes.map(RecipeMapper::convertRecipeToRecipeDto);
    }
}
