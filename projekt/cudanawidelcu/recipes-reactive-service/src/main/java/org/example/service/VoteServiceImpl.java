package org.example.service;

import org.example.dto.ProductDto;
import org.example.dto.VoteDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of VoteService that manages votes for recipes.
 */
@Service
public class VoteServiceImpl implements VoteService {

    private final WebClient.Builder builder;
    private final String VOTES_SERVICE_URL = "http://APPLICATION-GATEWAY/votes-service";

    public VoteServiceImpl(WebClient.Builder builder) {
        this.builder = builder;
    }

    /**
     * Retrieves all votes.
     *
     * @return a Flux emitting all votes
     */
    @Override
    public Flux<VoteDto> findAll() {
        return builder.build()
                .get()
                .uri(VOTES_SERVICE_URL + "/api/v1/votes")
                .retrieve()
                .bodyToFlux(VoteDto.class);
    }

    /**
     * Retrieves all votes for a recipe.
     *
     * @param recipeId the ID of the recipe
     * @return a Flux emitting all votes for the recipe
     */
    @Override
    public Flux<VoteDto> findAllByRecipe(Long recipeId) {
        return builder.build()
                .get()
                .uri(VOTES_SERVICE_URL + "/api/v1/votes/recipe/" + recipeId)
                .retrieve()
                .bodyToFlux(VoteDto.class);
    }

    /**
     * Saves a vote.
     *
     * @param voteDto the vote to save
     * @return a Mono emitting the saved vote
     */
    @Override
    public Mono<VoteDto> save(VoteDto voteDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return builder.build()
                .post()
                .uri(VOTES_SERVICE_URL + "/api/v1/votes")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(voteDto, VoteDto.class)
                .retrieve()
                .bodyToMono(VoteDto.class);
    }

    /**
     * Deletes all votes for a recipe.
     *
     * @param recipeId the ID of the recipe
     * @return a Mono emitting when the deletion is complete
     */
    @Override
    public Mono<Void> deleteAllByRecipeId(Long recipeId) {
        return builder.build()
                .delete()
                .uri(VOTES_SERVICE_URL + "/api/v1/votes/recipe/" + recipeId)
                .retrieve()
                .bodyToMono(Void.class);
    }
}