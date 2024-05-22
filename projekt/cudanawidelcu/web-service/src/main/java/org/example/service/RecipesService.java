package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.dto.RecipeDto;
import org.example.request.RateRecipeRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RecipesService {
	private final RestTemplate restTemplate;
	private final String RECIPES_SERVICE_URL = "http://APPLICATION-GATEWAY/recipes-service";

	protected Logger logger = Logger.getLogger(RecipesService.class
			.getName());

	public RecipesService(@LoadBalanced RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@PostConstruct
	public void init() {
		logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
	}

	public List<RecipeDto> getAll() {
		RecipeDto[] recipeDtos = null;

		try {
			String url = RECIPES_SERVICE_URL + "/api/v1/recipes";
			recipeDtos = restTemplate.getForObject(url, RecipeDto[].class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "getAll", e);
		}

		return (recipeDtos == null || recipeDtos.length == 0) ? null : Arrays.asList(recipeDtos);
	}

	public List<RecipeDto> getByCategory(String categoryName) {
		RecipeDto[] recipeDtos = null;

		try {
			String url = RECIPES_SERVICE_URL + "/api/v1/recipes/category/" + categoryName;
			recipeDtos = restTemplate.getForObject(url, RecipeDto[].class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "getByCategory", e);
		}

		return (recipeDtos == null || recipeDtos.length == 0) ? null : Arrays.asList(recipeDtos);
	}

	public RecipeDto get(Long id) {
		RecipeDto recipeDto = null;

		try {
			recipeDto = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes/" + id, RecipeDto.class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "get", e);
		}

		return recipeDto;
	}


	public RecipeDto rate(RateRecipeRequest rateRecipeRequest) {
		RecipeDto recipeDto = null;

		JSONObject rateRecipeJsonObject = new JSONObject();
		try {
			rateRecipeJsonObject.put("id", rateRecipeRequest.getId());
			rateRecipeJsonObject.put("name", rateRecipeRequest.getName());
			rateRecipeJsonObject.put("vote", rateRecipeRequest.getVote());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> requestEntity = new HttpEntity<>(rateRecipeJsonObject.toString(), headers);

			ResponseEntity<RecipeDto> responseEntity = restTemplate.exchange(
					RECIPES_SERVICE_URL + "/api/v1/recipes/rate",
					HttpMethod.POST,
					requestEntity,
					RecipeDto.class
			);

			recipeDto = responseEntity.getBody();
		} catch (Exception e) {
			throw new RuntimeException("Błąd podczas wysyłania zapytania POST: " + e.getMessage(), e);
		}

		return recipeDto;
	}
}
