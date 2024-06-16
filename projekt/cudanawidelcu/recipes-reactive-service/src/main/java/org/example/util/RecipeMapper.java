package org.example.util;



import org.example.dto.CategoryDto;
import org.example.dto.ProductDto;
import org.example.dto.RecipeDto;
import org.example.dto.VoteDto;
import org.example.model.Category;
import org.example.model.Product;
import org.example.model.Recipe;
import org.example.model.Vote;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between entities and DTOs related to recipes.
 */
public class RecipeMapper {

    /**
     * Converts a Recipe entity to a RecipeDto.
     *
     * @param recipe the Recipe entity to convert
     * @return the corresponding RecipeDto
     */
    public static RecipeDto convertRecipeToRecipeDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .category(CategoryDto.fromCategory(recipe.getCategory()))
                .products(recipe.getProducts().stream().map(RecipeMapper::convertProductToProductDto).collect(Collectors.toList()))
                .votes(recipe.getVotes().stream().map(RecipeMapper::convertVoteToVoteDto).collect(Collectors.toList()))
                .build();
    }

    /**
     * Converts a RecipeDto to a Recipe entity.
     *
     * @param recipeDto the RecipeDto to convert
     * @return the corresponding Recipe entity
     */
    public static Recipe convertRecipeDtoToRecipe(RecipeDto recipeDto) {
        return Recipe.builder()
                .id(recipeDto.getId())
                .name(recipeDto.getName())
                .description(recipeDto.getDescription())
                .category(Category.fromCategoryDto(recipeDto.getCategory()))
                .products(Optional.ofNullable(recipeDto.getProducts())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(RecipeMapper::convertProductDtoToProduct)
                        .collect(Collectors.toList()))
                .votes(Optional.ofNullable(recipeDto.getVotes())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(RecipeMapper::convertVoteDtoToVote)
                        .collect(Collectors.toList()))
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Converts a Product entity to a ProductDto.
     *
     * @param product the Product entity to convert
     * @return the corresponding ProductDto
     */
    public static ProductDto convertProductToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .recipeId(product.getRecipeId())
                .qty(product.getQty())
                .measure(product.getMeasure())
                .name(product.getName())
                .build();
    }

    /**
     * Converts a ProductDto to a Product entity.
     *
     * @param productDto the ProductDto to convert
     * @return the corresponding Product entity
     */
    public static Product convertProductDtoToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .recipeId(productDto.getRecipeId())
                .name(productDto.getName())
                .qty(productDto.getQty())
                .measure(productDto.getMeasure())
                .build();
    }

    /**
     * Converts a Vote entity to a VoteDto.
     *
     * @param vote the Vote entity to convert
     * @return the corresponding VoteDto
     */
    public static VoteDto convertVoteToVoteDto(Vote vote) {
        return VoteDto.builder()
                .id(vote.getId())
                .recipeId(vote.getRecipeId())
                .rating(vote.getRating())
                .build();
    }

    /**
     * Converts a VoteDto to a Vote entity.
     *
     * @param voteDto the VoteDto to convert
     * @return the corresponding Vote entity
     */
    public static Vote convertVoteDtoToVote(VoteDto voteDto) {
        return Vote.builder()
                .id(voteDto.getId())
                .recipeId(voteDto.getRecipeId())
                .rating(voteDto.getRating())
                .build();
    }
}