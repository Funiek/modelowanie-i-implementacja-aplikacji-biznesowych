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

/**
 * Implementation of RecipesService responsible for handling recipe-related operations.
 */
@Service
public class RecipesServiceImpl implements RecipesService {

	private final RestTemplate restTemplate;
	private final String RECIPES_SERVICE_URL = "http://APPLICATION-GATEWAY/recipes-service";

	protected Logger logger = Logger.getLogger(RecipesServiceImpl.class.getName());

	public RecipesServiceImpl(@LoadBalanced RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@PostConstruct
	public void init() {
		logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
	}

	/**
	 * Retrieves all recipes.
	 *
	 * @return list of RecipesFindAllResponse containing all recipes
	 */
	public List<RecipesFindAllResponse> findAll() {
		RecipesFindAllResponse[] recipes = null;

		try {
			recipes = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes", RecipesFindAllResponse[].class);
		} catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "findAll", e);
		}

		return (recipes == null || recipes.length == 0) ? new ArrayList<>() : Arrays.asList(recipes);
	}

	/**
	 * Retrieves all recipes by a specific category.
	 *
	 * @param categoryDto category by which to filter recipes
	 * @return list of RecipesFindAllByCategoryResponse containing recipes filtered by category
	 */
	public List<RecipesFindAllByCategoryResponse> findAllByCategory(CategoryDto categoryDto) {
		RecipesFindAllByCategoryResponse[] recipes = null;

		try {
			recipes = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes/category/" + categoryDto, RecipesFindAllByCategoryResponse[].class);
		} catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "findAllByCategory", e);
		}

		return (recipes == null || recipes.length == 0) ? new ArrayList<>() : Arrays.asList(recipes);
	}

	/**
	 * Saves a new recipe.
	 *
	 * @param recipeDto recipe to be saved
	 * @param jwtToken  JWT token for authentication
	 * @return RecipeDto object representing the newly saved recipe
	 * @throws RuntimeException if there's an error during the HTTP request
	 */
	public RecipeDto save(RecipeDto recipeDto, String jwtToken) throws RuntimeException {
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
			throw new RuntimeException("Error during POST request: " + e.getMessage(), e);
		}

		return newRecipeDto;
	}

	/**
	 * Retrieves a recipe by its ID.
	 *
	 * @param id ID of the recipe to retrieve
	 * @return RecipesFindByIdResponse containing the recipe details
	 */
	public RecipesFindByIdResponse findById(Long id) {
		RecipesFindByIdResponse recipe = null;

		try {
			recipe = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes/" + id, RecipesFindByIdResponse.class);
		} catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "findById", e);
		}

		return recipe;
	}

	/**
	 * Deletes a recipe by its ID.
	 *
	 * @param id    ID of the recipe to delete
	 * @param token JWT token for authentication
	 */
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

	/**
	 * Updates a recipe.
	 *
	 * @param id             ID of the recipe to update
	 * @param token          JWT token for authentication
	 * @param recipeToUpdate updated recipe details
	 * @return RecipesUpdateResponse containing updated recipe details
	 */
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