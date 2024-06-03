package org.example.repository;

import org.example.model.Vote;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VoteRepository extends R2dbcRepository<Vote, Long> {
    Flux<Vote> findAllByRecipeId(Long recipeId);
    Mono<Void> deleteAllByRecipeId(Long recipeId);
}
