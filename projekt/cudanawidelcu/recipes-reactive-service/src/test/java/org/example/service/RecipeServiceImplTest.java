package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.example.dto.ProductDto;
import org.example.dto.VoteDto;
import org.example.model.Category;
import org.example.model.Product;
import org.example.model.Recipe;
import org.example.model.Vote;
import org.example.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private ProductService productService;

    @Mock
    private VoteService voteService;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private Recipe recipe;
    private ProductDto productDto;
    private VoteDto voteDto;

    @BeforeEach
    void setUp() {
        productDto = ProductDto.builder()
                .id(1L)
                .recipeId(1L)
                .name("Test Product")
                .measure("kg")
                .qty(2.0)
                .build();

        voteDto = VoteDto.builder()
                .id(1L)
                .recipeId(1L)
                .rating(4.5)
                .build();

        recipe = Recipe.builder()
                .id(1L)
                .name("Test Recipe")
                .description("Test Recipe Description")
                .category(Category.LUNCH)
                .products(Collections.emptyList())
                .votes(Collections.emptyList())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void findAll_shouldReturnRecipes() {
        // Given
        when(recipeRepository.findAll()).thenReturn(Flux.just(recipe));
        when(productService.findAllByRecipe(anyLong())).thenReturn(Flux.just(productDto));
        when(voteService.findAllByRecipe(anyLong())).thenReturn(Flux.just(voteDto));

        // When
        Flux<Recipe> recipeFlux = recipeService.findAll();

        // Then
        StepVerifier.create(recipeFlux)
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getId()).isEqualTo(recipe.getId());
                    assertThat(result.getName()).isEqualTo(recipe.getName());
                    assertThat(result.getDescription()).isEqualTo(recipe.getDescription());
                    assertThat(result.getCategory().name()).isEqualTo(recipe.getCategory().name());
                    assertThat(result.getProducts()).hasSize(1);
                    assertThat(result.getProducts().get(0).getId()).isEqualTo(productDto.getId());
                    assertThat(result.getVotes()).hasSize(1);
                    assertThat(result.getVotes().get(0).getId()).isEqualTo(voteDto.getId());
                })
                .verifyComplete();

        verify(recipeRepository, times(1)).findAll();
        verify(productService, times(1)).findAllByRecipe(anyLong());
        verify(voteService, times(1)).findAllByRecipe(anyLong());
    }

    @Test
    void findAllByCategory_shouldReturnRecipesByCategory() {
        // Given
        Category category = Category.LUNCH;
        when(recipeRepository.findAllByCategory(category)).thenReturn(Flux.just(recipe));
        when(productService.findAllByRecipe(anyLong())).thenReturn(Flux.just(productDto));
        when(voteService.findAllByRecipe(anyLong())).thenReturn(Flux.just(voteDto));

        // When
        Flux<Recipe> recipeFlux = recipeService.findAllByCategory(category);

        // Then
        StepVerifier.create(recipeFlux)
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getId()).isEqualTo(recipe.getId());
                    assertThat(result.getName()).isEqualTo(recipe.getName());
                    assertThat(result.getDescription()).isEqualTo(recipe.getDescription());
                    assertThat(result.getCategory().name()).isEqualTo(recipe.getCategory().name());
                    assertThat(result.getProducts()).hasSize(1);
                    assertThat(result.getProducts().get(0).getId()).isEqualTo(productDto.getId());
                    assertThat(result.getVotes()).hasSize(1);
                    assertThat(result.getVotes().get(0).getId()).isEqualTo(voteDto.getId());
                })
                .verifyComplete();

        verify(recipeRepository, times(1)).findAllByCategory(category);
        verify(productService, times(1)).findAllByRecipe(anyLong());
        verify(voteService, times(1)).findAllByRecipe(anyLong());
    }

    @Test
    void save_shouldSaveRecipeInRepo() {
        // When
        when(recipeRepository.save(recipe)).thenReturn(Mono.just(recipe));

        Mono<Recipe> savedRecipe = recipeService.save(recipe);

        // Then
        StepVerifier.create(savedRecipe)
                .assertNext(result -> assertThat(result).isEqualTo(recipe))
                .verifyComplete();

        verify(recipeRepository, times(1)).save(recipe);
    }

    @Test
    void update_shouldUpdateRecipeInRepo() {
        // Given
        Long id = 1L;
        Recipe updatedRecipe = Recipe.builder()
                .id(id)
                .name("Updated Recipe")
                .description("Updated Recipe Description")
                .category(Category.LUNCH)
                .products(Collections.singletonList(Product.builder().id(2L).name("Updated Product").build()))
                .votes(Collections.singletonList(Vote.builder().id(2L).rating(5.0).build()))
                .createdAt(LocalDateTime.now())
                .build();

        when(recipeRepository.findById(id)).thenReturn(Mono.just(recipe));
        when(recipeRepository.save(updatedRecipe)).thenReturn(Mono.just(updatedRecipe));

        // When
        Mono<Recipe> updated = recipeService.update(id, updatedRecipe);

        // Then
        StepVerifier.create(updated)
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getId()).isEqualTo(id);
                    assertThat(result.getName()).isEqualTo(updatedRecipe.getName());
                    assertThat(result.getDescription()).isEqualTo(updatedRecipe.getDescription());
                    assertThat(result.getCategory().name()).isEqualTo(updatedRecipe.getCategory().name());
                    assertThat(result.getProducts()).hasSize(1);
                    assertThat(result.getProducts().get(0).getName()).isEqualTo("Updated Product");
                    assertThat(result.getVotes()).hasSize(1);
                    assertThat(result.getVotes().get(0).getRating()).isEqualTo(5.0);
                })
                .verifyComplete();

        verify(recipeRepository, times(1)).findById(id);
        verify(recipeRepository, times(1)).save(updatedRecipe);
    }

    @Test
    void deleteById_shouldDeleteRecipeAndRelatedEntities() {
        // Given
        Long id = 1L;
        when(recipeRepository.deleteById(id)).thenReturn(Mono.empty());
        when(productService.deleteAllByRecipeId(id)).thenReturn(Mono.empty());
        when(voteService.deleteAllByRecipeId(id)).thenReturn(Mono.empty());

        // When
        Mono<Void> result = recipeService.deleteById(id);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(recipeRepository, times(1)).deleteById(id);
        verify(productService, times(1)).deleteAllByRecipeId(id);
        verify(voteService, times(1)).deleteAllByRecipeId(id);
    }

    @Test
    void findById_shouldReturnRecipeWithProductsAndVotes() {
        // Given
        Long id = 1L;
        when(recipeRepository.findById(id)).thenReturn(Mono.just(recipe));
        when(productService.findAllByRecipe(anyLong())).thenReturn(Flux.just(productDto));
        when(voteService.findAllByRecipe(anyLong())).thenReturn(Flux.just(voteDto));

        // When
        Mono<Recipe> foundRecipe = recipeService.findById(id);

        // Then
        StepVerifier.create(foundRecipe)
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getId()).isEqualTo(recipe.getId());
                    assertThat(result.getName()).isEqualTo(recipe.getName());
                    assertThat(result.getDescription()).isEqualTo(recipe.getDescription());
                    assertThat(result.getCategory().name()).isEqualTo(recipe.getCategory().name());
                    assertThat(result.getProducts()).hasSize(1);
                    assertThat(result.getProducts().get(0).getId()).isEqualTo(productDto.getId());
                    assertThat(result.getVotes()).hasSize(1);
                    assertThat(result.getVotes().get(0).getId()).isEqualTo(voteDto.getId());
                })
                .verifyComplete();

        verify(recipeRepository, times(1)).findById(id);
        verify(productService, times(1)).findAllByRecipe(anyLong());
        verify(voteService, times(1)).findAllByRecipe(anyLong());
    }
}