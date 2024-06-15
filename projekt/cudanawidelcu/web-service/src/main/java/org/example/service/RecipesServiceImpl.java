package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.dto.CategoryDto;
import org.example.dto.RecipeDto;
import org.example.response.*;
import org.example.request.RecipesUpdateRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RecipesServiceImpl implements RecipesService {
	private final RestTemplate restTemplate;
	private final String RECIPES_SERVICE_URL = "http://APPLICATION-GATEWAY/recipes-service";

	protected Logger logger = Logger.getLogger(RecipesServiceImpl.class
			.getName());

	public RecipesServiceImpl(@LoadBalanced RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@PostConstruct
	public void init() {
		logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
	}

	public List<RecipesFindAllResponse> findAll() {
		RecipesFindAllResponse[] recipes = null;

		try {
			recipes = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes", RecipesFindAllResponse[].class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "getAll", e);
		}

		return (recipes == null || recipes.length == 0) ? new ArrayList<>() : Arrays.asList(recipes);
	}

	public List<RecipesFindAllByCategoryResponse> findAllByCategory(CategoryDto categoryDto) {
		RecipesFindAllByCategoryResponse[] recipes = null;

		try {
			recipes = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes/category/" + categoryDto, RecipesFindAllByCategoryResponse[].class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "getByCategory", e);
		}

		return (recipes == null || recipes.length == 0) ? new ArrayList<>() : Arrays.asList(recipes);
	}

	public RecipeDto save(RecipeDto recipeDto, String jwtToken) {
		RecipeDto newRecipeDto = null;

		JSONObject newRecipeJsonObject = new JSONObject();
		try {
			newRecipeJsonObject.put("name", recipeDto.getName());
			newRecipeJsonObject.put("description", recipeDto.getDescription());
			newRecipeJsonObject.put("category", recipeDto.getCategory());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(jwtToken);

			HttpEntity<String> requestEntity = new HttpEntity<>(newRecipeJsonObject.toString(), headers);

			ResponseEntity<RecipeDto> responseEntity = restTemplate.exchange(
					RECIPES_SERVICE_URL + "/api/v1/recipes",
					HttpMethod.POST,
					requestEntity,
					RecipeDto.class
			);

			newRecipeDto = responseEntity.getBody();
		} catch (Exception e) {
			throw new RuntimeException("Błąd podczas wysyłania zapytania POST: " + e.getMessage(), e);
		}

		return newRecipeDto;
	}

	public RecipesFindByIdResponse findById(Long id) {
		RecipesFindByIdResponse recipe = null;

		try {
			recipe = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes/" + id, RecipesFindByIdResponse.class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "findById", e);
		}

		return recipe;
	}


	public void delete(Long id, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		restTemplate.exchange(
				RECIPES_SERVICE_URL + "/api/v1/recipes/" + id,
				HttpMethod.DELETE,
				requestEntity,
				IdentityValidateAdminResponse.class
		);
	}

	public RecipesUpdateResponse update(Long id, String token, RecipesUpdateRequest recipeToUpdate) {
		RecipesUpdateResponse recipe;

		JSONObject updateRecipeJsonObject = new JSONObject();
		try {
			updateRecipeJsonObject.put("id", recipeToUpdate.getId());
			updateRecipeJsonObject.put("name", recipeToUpdate.getName());
			updateRecipeJsonObject.put("description", recipeToUpdate.getDescription());
			updateRecipeJsonObject.put("category", recipeToUpdate.getCategory().name());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(updateRecipeJsonObject.toString(), headers);

		ResponseEntity<RecipesUpdateResponse> responseEntity = restTemplate.exchange(
				RECIPES_SERVICE_URL + "/api/v1/recipes/" + id,
				HttpMethod.PUT,
				requestEntity,
				RecipesUpdateResponse.class
		);

		recipe = responseEntity.getBody();

		return recipe;
	}
}
