package org.example.service;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.example.model.Vote;
import org.example.repository.VoteRepository;
import org.example.response.RatingByRecipeIdResponse;
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
import java.util.List;

@ExtendWith(MockitoExtension.class)
class VoteServiceImplTest {
    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private VoteServiceImpl voteService;

    private Vote vote;

    @BeforeEach
    void setUp() {
        vote = Vote.builder()
                .id(1L)
                .recipeId(2L)
                .rating(4.5)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void findAll_shouldReturnVotes() {
        // When
        when(voteRepository.findAll()).thenReturn(Flux.just(vote));

        Flux<Vote> voteFlux = voteService.findAll();

        StepVerifier.create(voteFlux)
                .expectNext(vote)
                .verifyComplete();

        // Then
        verify(voteRepository, times(1)).findAll();
    }

    @Test
    void findAllByRecipe_shouldReturnVotesByRecipeId() {
        // Given
        Long recipeId = 2L;

        // When
        when(voteRepository.findAllByRecipeId(recipeId)).thenReturn(Flux.just(vote));

        Flux<Vote> voteFlux = voteService.findAllByRecipe(recipeId);

        StepVerifier.create(voteFlux)
                .expectNext(vote)
                .verifyComplete();

        // Then
        verify(voteRepository, times(1)).findAllByRecipeId(recipeId);
    }

    @Test
    void save_shouldSaveVoteInRepo() {
        // When
        when(voteRepository.save(vote)).thenReturn(Mono.just(vote));

        Mono<Vote> savedVote = voteService.save(vote);

        StepVerifier.create(savedVote)
                .expectNext(vote)
                .verifyComplete();

        // Then
        verify(voteRepository, times(1)).save(vote);
    }

    @Test
    void deleteAllByRecipeId_shouldDeleteVotesByRecipeIdInRepo() {
        // Given
        Long recipeId = 2L;

        // When
        when(voteRepository.deleteAllByRecipeId(recipeId)).thenReturn(Mono.empty());

        Mono<Void> result = voteService.deleteAllByRecipeId(recipeId);

        StepVerifier.create(result)
                .verifyComplete();

        // Then
        verify(voteRepository, times(1)).deleteAllByRecipeId(recipeId);
    }

    @Test
    void ratingByRecipeId_shouldReturnCorrectRating() {
        // Given
        Long recipeId = 2L;
        List<Vote> votes = Arrays.asList(
                Vote.builder().id(1L).recipeId(recipeId).rating(4.0).createdAt(LocalDateTime.now()).build(),
                Vote.builder().id(2L).recipeId(recipeId).rating(5.0).createdAt(LocalDateTime.now()).build()
        );

        // When
        when(voteRepository.findAllByRecipeId(recipeId)).thenReturn(Flux.fromIterable(votes));

        Mono<RatingByRecipeIdResponse> responseMono = voteService.ratingByRecipeId(recipeId);

        // Then
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getRecipeId()).isEqualTo(recipeId);
                    assertThat(response.getRating()).isEqualTo(4.5);
                    assertThat(response.getCountVotes()).isEqualTo(2);
                })
                .verifyComplete();

        verify(voteRepository, times(1)).findAllByRecipeId(recipeId);
    }
}