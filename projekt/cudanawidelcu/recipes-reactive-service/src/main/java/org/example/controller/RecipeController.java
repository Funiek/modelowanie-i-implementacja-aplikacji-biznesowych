package org.example.controller;

import org.example.dto.RecipeDto;
import org.example.model.Category;
import org.example.model.Recipe;
import org.example.service.RecipeService;
import org.example.util.RecipeMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller class for managing Recipe entities.
 */
@RestController
@EnableWebFlux
@RequestMapping("/api/v1/recipes")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * Constructs a new RecipeController with the given RecipeService.
     *
     * @param recipeService the RecipeService to be used
     */
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Retrieves all recipes.
     *
     * @return a Flux emitting RecipeDto objects
     */
    @GetMapping
    public Flux<RecipeDto> findAll() {
        Flux<Recipe> recipes = recipeService.findAll();
        return recipes.flatMap(recipe -> Mono.just(RecipeMapper.convertRecipeToRecipeDto(recipe)));
    }

    /**
     * Retrieves all recipes belonging to a specific category.
     *
     * @param category the category of recipes to retrieve
     * @return a Flux emitting RecipeDto objects
     */
    @GetMapping("category/{categoryName}")
    public Flux<RecipeDto> findAllByCategory(@PathVariable("categoryName") Category category) {
        Flux<Recipe> recipes = recipeService.findAllByCategory(category);
        return recipes.flatMap(recipe -> Mono.just(RecipeMapper.convertRecipeToRecipeDto(recipe)));
    }

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id the ID of the recipe to retrieve
     * @return a Mono emitting a RecipeDto object
     */
    @GetMapping("/{id}")
    public Mono<RecipeDto> findById(@PathVariable("id") Long id) {
        Mono<Recipe> recipe = recipeService.findById(id);
        return recipe.map(RecipeMapper::convertRecipeToRecipeDto);
    }

    /**
     * Saves a new recipe.
     *
     * @param recipeDto the RecipeDto object representing the recipe to save
     * @return a Mono emitting a RecipeDto object representing the saved recipe
     */
    @PostMapping
    public Mono<RecipeDto> save(@RequestBody RecipeDto recipeDto) {
        Recipe recipe = RecipeMapper.convertRecipeDtoToRecipe(recipeDto);
        Mono<Recipe> createdRecipe = recipeService.save(recipe);
        return createdRecipe.map(RecipeMapper::convertRecipeToRecipeDto);
    }

    /**
     * Updates an existing recipe.
     *
     * @param id        the ID of the recipe to update
     * @param recipeDto the RecipeDto object representing the updated recipe
     * @return a Mono emitting a RecipeDto object representing the updated recipe
     */
    @PutMapping("/{id}")
    public Mono<RecipeDto> update(@PathVariable("id") Long id, @RequestBody RecipeDto recipeDto) {
        Recipe recipe = RecipeMapper.convertRecipeDtoToRecipe(recipeDto);
        Mono<Recipe> updatedRecipe = recipeService.update(id, recipe);
        return updatedRecipe.map(RecipeMapper::convertRecipeToRecipeDto);
    }

    /**
     * Deletes a recipe by its ID.
     *
     * @param id the ID of the recipe to delete
     * @return a Mono emitting void
     */
    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable("id") Long id) {
        return recipeService.deleteById(id);
    }
}