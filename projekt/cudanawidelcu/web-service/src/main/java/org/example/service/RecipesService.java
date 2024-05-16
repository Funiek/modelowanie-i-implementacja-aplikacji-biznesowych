package org.example.service;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.example.dto.RecipeDto;
import org.example.request.RateRecipeRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RecipesService {
	protected final RestTemplate restTemplate;

	protected final String RECIPES_SERVICE_URL = "http://RECIPES-SERVICE";

	protected Logger logger = Logger.getLogger(RecipesService.class
			.getName());

	public RecipesService(@LoadBalanced RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@PostConstruct
	public void demoOnly() {
		logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
	}

	public List<RecipeDto> getAll() {
		RecipeDto[] recipeDtos = null;

		try {
			recipeDtos = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes", RecipeDto[].class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), this.getClass().getEnclosingMethod().getName(), e);
		}

		return (recipeDtos == null || recipeDtos.length == 0) ? null : Arrays.asList(recipeDtos);
	}

	public RecipeDto getByName(String name) {
		RecipeDto recipeDto = null;

		try {
			recipeDto = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes/" + name, RecipeDto.class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), this.getClass().getEnclosingMethod().getName(), e);
		}

		return recipeDto;
	}

	@SneakyThrows
	public RecipeDto rate(RateRecipeRequest rateRecipeRequest) {
		RecipeDto recipeDto = null;

		JSONObject rateRecipeJsonObject = new JSONObject();
		rateRecipeJsonObject.put("name", rateRecipeRequest.getName());
		rateRecipeJsonObject.put("vote", rateRecipeRequest.getVote());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(rateRecipeJsonObject.toString(), headers);

		try {
			recipeDto = restTemplate.postForObject(RECIPES_SERVICE_URL + "/api/v1/recipes/rate/", request, RecipeDto.class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "rate", e);
		}

		return recipeDto;
	}
}
