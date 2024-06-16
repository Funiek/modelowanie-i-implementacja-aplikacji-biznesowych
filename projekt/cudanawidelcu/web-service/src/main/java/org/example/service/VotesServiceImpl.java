package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.request.VotesSaveRequest;
import org.example.response.VotesRatingByRecipeIdResponse;
import org.example.response.VotesSaveResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

/**
 * Implementation of VotesService responsible for handling voting-related operations.
 */
@Service
public class VotesServiceImpl implements VotesService {

    private final RestTemplate restTemplate;
    private final String VOTES_SERVICE_URL = "http://APPLICATION-GATEWAY/votes-service";
    protected Logger logger = Logger.getLogger(VotesServiceImpl.class.getName());

    public VotesServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
    }

    /**
     * Retrieves rating information for a recipe by its ID.
     *
     * @param recipeId ID of the recipe to retrieve rating for
     * @return VotesRatingByRecipeIdResponse containing rating information
     */
    public VotesRatingByRecipeIdResponse ratingByRecipeId(Long recipeId) {
        return restTemplate.getForObject(VOTES_SERVICE_URL + "/api/v1/votes/rating/recipe/" + recipeId, VotesRatingByRecipeIdResponse.class);
    }

    /**
     * Saves a new vote for a recipe.
     *
     * @param votesSaveRequest request containing recipe ID and rating to be saved
     * @return VotesSaveResponse containing the response from the votes service
     * @throws RuntimeException if there's an error during the HTTP request
     */
    public VotesSaveResponse save(VotesSaveRequest votesSaveRequest) throws RuntimeException {
        VotesSaveResponse votesSaveResponse;

        JSONObject rateRecipeJsonObject = new JSONObject();
        try {
            rateRecipeJsonObject.put("recipeId", votesSaveRequest.getRecipeId());
            rateRecipeJsonObject.put("rating", (double) votesSaveRequest.getRating());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(rateRecipeJsonObject.toString(), headers);

            ResponseEntity<VotesSaveResponse> responseEntity = restTemplate.exchange(
                    VOTES_SERVICE_URL + "/api/v1/votes",
                    HttpMethod.POST,
                    requestEntity,
                    VotesSaveResponse.class
            );

            votesSaveResponse = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error during POST request: " + e.getMessage(), e);
        }

        return votesSaveResponse;
    }
}