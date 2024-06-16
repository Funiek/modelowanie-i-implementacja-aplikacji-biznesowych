package org.example.service;

import org.example.exception.RecipeNotFoundException;
import org.example.model.Category;
import org.example.model.Product;
import org.example.model.Recipe;
import org.example.model.Vote;
import org.example.repository.RecipeRepository;
import org.example.util.RecipeMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of RecipeService that manages recipes, products, and votes.
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final ProductService productService;
    private final VoteService voteService;

    public RecipeServiceImpl(RecipeRepository recipeRepository, ProductService productService, VoteService voteService) {
        this.recipeRepository = recipeRepository;
        this.productService = productService;
        this.voteService = voteService;
    }

    /**
     * Retrieves all recipes with associated products and votes.
     *
     * @return a Flux emitting all recipes
     */
    @Override
    public Flux<Recipe> findAll() {
        return recipeRepository.findAll()
                .flatMap(recipe -> productService.findAllByRecipe(recipe.getId())
                        .collectList()
                        .map(productDtos -> {
                            List<Product> products = productDtos.stream()
                                    .map(RecipeMapper::convertProductDtoToProduct)
                                    .collect(Collectors.toList());
                            recipe.setProducts(products);
                            return recipe;
                        })
                )
                .flatMap(recipe -> voteService.findAllByRecipe(recipe.getId())
                        .collectList()
                        .map(voteDtos -> {
                            List<Vote> votes = voteDtos.stream()
                                    .map(RecipeMapper::convertVoteDtoToVote)
                                    .collect(Collectors.toList());
                            recipe.setVotes(votes);
                            return recipe;
                        })
                );
    }

    /**
     * Retrieves all recipes belonging to a specific category with associated products and votes.
     *
     * @param category the category of the recipes
     * @return a Flux emitting all recipes in the given category
     */
    @Override
    public Flux<Recipe> findAllByCategory(Category category) {
        return recipeRepository.findAllByCategory(category)
                .flatMap(recipe -> productService.findAllByRecipe(recipe.getId())
                        .collectList()
                        .map(productDtos -> {
                            List<Product> products = productDtos.stream()
                                    .map(RecipeMapper::convertProductDtoToProduct)
                                    .collect(Collectors.toList());
                            recipe.setProducts(products);
                            return recipe;
                        })
                )
                .flatMap(recipe -> voteService.findAllByRecipe(recipe.getId())
                        .collectList()
                        .map(voteDtos -> {
                            List<Vote> votes = voteDtos.stream()
                                    .map(RecipeMapper::convertVoteDtoToVote)
                                    .collect(Collectors.toList());
                            recipe.setVotes(votes);
                            return recipe;
                        })
                );
    }

    /**
     * Saves a recipe.
     *
     * @param recipe the recipe to save
     * @return a Mono emitting the saved recipe
     */
    @Override
    public Mono<Recipe> save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    /**
     * Updates a recipe with the given ID.
     *
     * @param id     the ID of the recipe to update
     * @param recipe the updated recipe
     * @return a Mono emitting the updated recipe
     */
    @Override
    public Mono<Recipe> update(Long id, Recipe recipe) {
        return recipeRepository.findById(id)
                .flatMap(existingRecipe -> recipeRepository.save(recipe))
                .switchIfEmpty(Mono.error(new RecipeNotFoundException(recipe.getName())));
    }

    /**
     * Deletes a recipe by its ID, along with associated products and votes.
     *
     * @param id the ID of the recipe to delete
     * @return a Mono emitting when the deletion is complete
     */
    @Override
    public Mono<Void> deleteById(Long id) {
        return recipeRepository.deleteById(id)
                .then(productService.deleteAllByRecipeId(id))
                .then(voteService.deleteAllByRecipeId(id));
    }

    /**
     * Retrieves a recipe by its ID with associated products and votes.
     *
     * @param id the ID of the recipe
     * @return a Mono emitting the recipe
     */
    @Override
    public Mono<Recipe> findById(Long id) {
        return recipeRepository.findById(id)
                .flatMap(recipe -> productService.findAllByRecipe(recipe.getId())
                        .collectList()
                        .map(productDtos -> {
                            List<Product> products = productDtos.stream()
                                    .map(RecipeMapper::convertProductDtoToProduct)
                                    .collect(Collectors.toList());
                            recipe.setProducts(products);
                            return recipe;
                        })
                )
                .flatMap(recipe -> voteService.findAllByRecipe(recipe.getId())
                        .collectList()
                        .map(voteDtos -> {
                            List<Vote> votes = voteDtos.stream()
                                    .map(RecipeMapper::convertVoteDtoToVote)
                                    .collect(Collectors.toList());
                            recipe.setVotes(votes);
                            return recipe;
                        })
                );
    }
}