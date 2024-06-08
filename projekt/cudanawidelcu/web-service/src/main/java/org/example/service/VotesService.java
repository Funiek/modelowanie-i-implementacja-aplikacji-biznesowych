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

    public VotesSaveResponse save(VotesSaveRequest votesSaveRequest) {
        VotesSaveResponse votesSaveResponse = null;

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
            throw new RuntimeException("Błąd podczas wysyłania zapytania POST: " + e.getMessage(), e);
        }

        return votesSaveResponse;
    }

}
