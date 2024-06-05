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
import java.util.List;
import java.util.stream.Collectors;

public class RecipeMapper {
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

    public static Recipe convertRecipeDtoToRecipe(RecipeDto recipeDto) {
        return Recipe.builder()
                .id(recipeDto.getId())
                .name(recipeDto.getName())
                .description(recipeDto.getDescription())
                .category(Category.fromCategoryDto(recipeDto.getCategory()))
                .products(recipeDto.getProducts()
                        .stream()
                        .map(RecipeMapper::convertProductDtoToProduct)
                        .collect(Collectors.toList()))
                .votes(recipeDto.getVotes()
                        .stream()
                        .map(RecipeMapper::convertVoteDtoToVote)
                        .collect(Collectors.toList()))
                .createdAt(LocalDateTime.now())
                .build();
    }


    public static ProductDto convertProductToProductDto(Product product) {
        return ProductDto.builder()
                        .id(product.getId())
                        .recipeId(product.getRecipeId())
                        .qty(product.getQty())
                        .measure(product.getMeasure())
                        .name(product.getName())
                        .build();
    }
    public static Product convertProductDtoToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .recipeId(productDto.getRecipeId())
                .name(productDto.getName())
                .qty(productDto.getQty())
                .measure(productDto.getMeasure())
                .build();
    }

    public static VoteDto convertVoteToVoteDto(Vote vote) {
        return VoteDto.builder()
                .id(vote.getId())
                .recipeId(vote.getRecipeId())
                .rating(vote.getRating())
                .build();
    }

    public static Vote convertVoteDtoToVote(VoteDto voteDto) {
        Vote vote = new Vote();
        vote.setId(voteDto.getId());
        vote.setRecipeId(voteDto.getRecipeId());
        vote.setRating(voteDto.getRating());

        return vote;
    }

}
