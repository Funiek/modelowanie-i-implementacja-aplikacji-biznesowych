package org.example.service;

import org.example.model.Vote;
import org.example.repository.VoteRepository;
import org.example.response.RatingByRecipeIdResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service implementation for managing votes.
 */
@Service
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;

    public VoteServiceImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    /**
     * Retrieves all votes.
     *
     * @return a Flux emitting all votes
     */
    @Override
    public Flux<Vote> findAll() {
        return voteRepository.findAll();
    }

    /**
     * Retrieves all votes by recipe ID.
     *
     * @param recipeId the ID of the recipe
     * @return a Flux emitting all votes with the given recipe ID
     */
    @Override
    public Flux<Vote> findAllByRecipe(Long recipeId) {
        return voteRepository.findAllByRecipeId(recipeId);
    }

    /**
     * Saves a vote.
     *
     * @param vote the vote to be saved
     * @return a Mono emitting the saved vote
     */
    @Override
    public Mono<Vote> save(Vote vote) {
        return voteRepository.save(vote);
    }

    /**
     * Deletes all votes by recipe ID.
     *
     * @param recipeId the ID of the recipe
     * @return a Mono signaling when the operation completes (void)
     */
    @Override
    public Mono<Void> deleteAllByRecipeId(Long recipeId) {
        return voteRepository.deleteAllByRecipeId(recipeId);
    }

    /**
     * Retrieves rating statistics (average rating and number of votes) for a recipe.
     *
     * @param recipeId the ID of the recipe
     * @return a Mono emitting the rating statistics for the recipe
     */
    @Override
    public Mono<RatingByRecipeIdResponse> ratingByRecipeId(Long recipeId) {
        return voteRepository.findAllByRecipeId(recipeId)
                .collectList()
                .map(votes -> {
                    double totalRating = votes.stream().mapToDouble(Vote::getRating).sum();
                    int countVotes = votes.size();
                    double averageRating = countVotes > 0 ? totalRating / countVotes : 0.0;
                    return RatingByRecipeIdResponse.builder()
                            .recipeId(recipeId)
                            .rating(averageRating)
                            .countVotes(countVotes)
                            .build();
                });
    }
}