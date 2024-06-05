package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.request.RecipesFindAllRequest;
import org.example.response.VotesRatingByRecipeIdResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
public class VotesService {
    private final RestTemplate restTemplate;
    private final String VOTES_SERVICE_URL = "http://APPLICATION-GATEWAY/votes-service";
    protected Logger logger = Logger.getLogger(RecipesService.class
            .getName());

    public VotesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
    }

    public VotesRatingByRecipeIdResponse ratingByRecipeId(Long recipeId) {
        return restTemplate.getForObject(VOTES_SERVICE_URL + "/api/v1/votes/rating/recipe/" + recipeId, VotesRatingByRecipeIdResponse.class);
    }
}
