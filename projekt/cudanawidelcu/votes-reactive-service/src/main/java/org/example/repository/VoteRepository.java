package org.example.repository;

import org.example.model.Vote;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository for performing CRUD operations on votes.
 */
@Repository
public interface VoteRepository extends R2dbcRepository<Vote, Long> {

    /**
     * Finds all votes by recipe ID.
     *
     * @param recipeId the ID of the recipe
     * @return a Flux emitting all votes with the given recipe ID
     */
    Flux<Vote> findAllByRecipeId(Long recipeId);

    /**
     * Deletes all votes by recipe ID.
     *
     * @param recipeId the ID of the recipe
     * @return a Mono signaling when the operation completes (void)
     */
    Mono<Void> deleteAllByRecipeId(Long recipeId);
}