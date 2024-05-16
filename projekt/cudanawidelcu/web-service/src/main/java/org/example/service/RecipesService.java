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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RecipesService {
	protected final RestTemplate restTemplate;

	protected final String RECIPES_SERVICE_URL = "http://APPLICATION-GATEWAY/recipes-service";

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

	public RecipeDto getByName(String name) {
		RecipeDto recipeDto = null;

		try {
			recipeDto = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes/" + name, RecipeDto.class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "getByName", e);
		}

		return recipeDto;
	}


	public RecipeDto rate(RateRecipeRequest rateRecipeRequest) {
		RecipeDto recipeDto = null;

		JSONObject rateRecipeJsonObject = new JSONObject();
		try {
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
