package org.example.util;
import static org.assertj.core.api.Assertions.assertThat;

import org.example.dto.CategoryDto;
import org.example.dto.ProductDto;
import org.example.dto.RecipeDto;
import org.example.dto.VoteDto;
import org.example.model.Category;
import org.example.model.Product;
import org.example.model.Recipe;
import org.example.model.Vote;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
public class RecipeMapperTest {

    @Test
    void convertRecipeToRecipeDto_ShouldReturnRecipeDto() {
        // Given
        Recipe recipe = Recipe.builder()
                .id(1L)
                .name("Test Recipe")
                .description("Test Recipe Description")
                .category(Category.LUNCH)
                .products(Arrays.asList(
                        Product.builder().id(1L).recipeId(1L).name("Product 1").measure("kg").qty(2.0).build(),
                        Product.builder().id(2L).recipeId(1L).name("Product 2").measure("g").qty(500.0).build()
                ))
                .votes(Collections.singletonList(
                        Vote.builder().id(1L).recipeId(1L).rating(4.5).build()
                ))
                .createdAt(LocalDateTime.now())
                .build();

        // When
        RecipeDto recipeDto = RecipeMapper.convertRecipeToRecipeDto(recipe);

        // Then
        assertThat(recipeDto).isNotNull();
        assertThat(recipeDto.getId()).isEqualTo(recipe.getId());
        assertThat(recipeDto.getName()).isEqualTo(recipe.getName());
        assertThat(recipeDto.getDescription()).isEqualTo(recipe.getDescription());
        assertThat(recipeDto.getCategory()).isNotNull();
        assertThat(recipeDto.getCategory().name()).isEqualTo(recipe.getCategory().name());

        assertThat(recipeDto.getProducts()).hasSize(2);
        assertThat(recipeDto.getProducts().get(0).getId()).isEqualTo(recipe.getProducts().get(0).getId());
        assertThat(recipeDto.getProducts().get(0).getName()).isEqualTo(recipe.getProducts().get(0).getName());
        assertThat(recipeDto.getProducts().get(0).getMeasure()).isEqualTo(recipe.getProducts().get(0).getMeasure());
        assertThat(recipeDto.getProducts().get(0).getQty()).isEqualTo(recipe.getProducts().get(0).getQty());

        assertThat(recipeDto.getVotes()).hasSize(1);
        assertThat(recipeDto.getVotes().get(0).getId()).isEqualTo(recipe.getVotes().get(0).getId());
        assertThat(recipeDto.getVotes().get(0).getRecipeId()).isEqualTo(recipe.getVotes().get(0).getRecipeId());
        assertThat(recipeDto.getVotes().get(0).getRating()).isEqualTo(recipe.getVotes().get(0).getRating());
    }

    @Test
    void convertRecipeDtoToRecipe_ShouldReturnRecipe() {
        // Given
        RecipeDto recipeDto = RecipeDto.builder()
                .id(1L)
                .name("Test Recipe")
                .description("Test Recipe Description")
                .category(CategoryDto.LUNCH)
                .products(Arrays.asList(
                        ProductDto.builder().id(1L).recipeId(1L).name("Product 1").measure("kg").qty(2.0).build(),
                        ProductDto.builder().id(2L).recipeId(1L).name("Product 2").measure("g").qty(500.0).build()
                ))
                .votes(Collections.singletonList(
                        VoteDto.builder().id(1L).recipeId(1L).rating(4.5).build()
                ))
                .build();

        // When
        Recipe recipe = RecipeMapper.convertRecipeDtoToRecipe(recipeDto);

        // Then
        assertThat(recipe).isNotNull();
        assertThat(recipe.getId()).isEqualTo(recipeDto.getId());
        assertThat(recipe.getName()).isEqualTo(recipeDto.getName());
        assertThat(recipe.getDescription()).isEqualTo(recipeDto.getDescription());
        assertThat(recipe.getCategory()).isNotNull();
        assertThat(recipe.getCategory().name()).isEqualTo(recipeDto.getCategory().name());

        assertThat(recipe.getProducts()).hasSize(2);
        assertThat(recipe.getProducts().get(0).getId()).isEqualTo(recipeDto.getProducts().get(0).getId());
        assertThat(recipe.getProducts().get(0).getName()).isEqualTo(recipeDto.getProducts().get(0).getName());
        assertThat(recipe.getProducts().get(0).getMeasure()).isEqualTo(recipeDto.getProducts().get(0).getMeasure());
        assertThat(recipe.getProducts().get(0).getQty()).isEqualTo(recipeDto.getProducts().get(0).getQty());

        assertThat(recipe.getVotes()).hasSize(1);
        assertThat(recipe.getVotes().get(0).getId()).isEqualTo(recipeDto.getVotes().get(0).getId());
        assertThat(recipe.getVotes().get(0).getRecipeId()).isEqualTo(recipeDto.getVotes().get(0).getRecipeId());
        assertThat(recipe.getVotes().get(0).getRating()).isEqualTo(recipeDto.getVotes().get(0).getRating());
        assertThat(recipe.getCreatedAt()).isNotNull();
    }
}