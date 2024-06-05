package org.example.controller;

import org.example.dto.RecipeDto;
import org.example.model.Recipe;
import org.example.service.RecipeService;
import org.example.util.RecipeMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        Flux<Recipe> recipes = recipeService.findAll();
        return recipes.flatMap(recipe -> Mono.just(RecipeMapper.convertRecipeToRecipeDto(recipe)));
    }

    @PostMapping
    public Mono<RecipeDto> save(@RequestBody RecipeDto recipeDto) {
        Recipe recipe = RecipeMapper.convertRecipeDtoToRecipe(recipeDto);
        Mono<Recipe> createdRecipe = recipeService.save(recipe);
        return createdRecipe.map(RecipeMapper::convertRecipeToRecipeDto);
    }

    @PutMapping("/{id}")
    public Mono<RecipeDto> update(@PathVariable("id") Long id, @RequestBody RecipeDto recipeDto) {
        Recipe recipe = RecipeMapper.convertRecipeDtoToRecipe(recipeDto);
        Mono<Recipe> createdRecipe = recipeService.update(id, recipe);
        return createdRecipe.map(RecipeMapper::convertRecipeToRecipeDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable("id") Long id) {
        return recipeService.deleteById(id);
    }
}
