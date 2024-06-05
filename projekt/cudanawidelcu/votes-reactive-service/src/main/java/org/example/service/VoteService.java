package org.example.service;

import org.example.model.Vote;
import org.example.response.RatingByRecipeIdResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoteService {
    Flux<Vote> findAll();

    Flux<Vote> findAllByRecipe(Long recipeId);

    Mono<Vote> save(Vote vote);

    Mono<Void> deleteAllByRecipeId(Long recipeId);

    Mono<RatingByRecipeIdResponse> ratingByRecipeId(Long recipeId);
}
