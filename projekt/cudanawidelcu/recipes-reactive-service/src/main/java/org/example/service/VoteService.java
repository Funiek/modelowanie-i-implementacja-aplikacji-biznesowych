package org.example.service;

import org.example.dto.ProductDto;
import org.example.dto.VoteDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoteService {
    Flux<VoteDto> findAll();

    Flux<VoteDto> findAllByRecipe(Long recipeId);

    Mono<VoteDto> save(VoteDto voteDto);

    Mono<Void> deleteAllByRecipeId(Long recipeId);
}
