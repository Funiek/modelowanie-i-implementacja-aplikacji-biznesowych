package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.dto.CategoryDto;
import org.example.dto.ProductDto;
import org.example.dto.RecipeDto;
import org.example.dto.VoteDto;
import org.example.response.RecipesFindAllByCategoryResponse;
import org.example.response.RecipesFindAllResponse;
import org.example.response.RecipesFindByIdResponse;
import org.example.request.RecipesUpdateRequest;
import org.example.response.IdentityValidateAdminResponse;
import org.json.JSONArray;
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

	public List<RecipesFindAllResponse> findAll() {
		RecipesFindAllResponse[] recipes = null;

		try {
			recipes = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes", RecipesFindAllResponse[].class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "getAll", e);
		}

		return (recipes == null || recipes.length == 0) ? null : Arrays.asList(recipes);
	}

	public List<RecipesFindAllByCategoryResponse> findAllByCategory(CategoryDto categoryDto) {
		RecipesFindAllByCategoryResponse[] recipes = null;

		try {
			recipes = restTemplate.getForObject(RECIPES_SERVICE_URL + "/api/v1/recipes/category/" + categoryDto, RecipesFindAllByCategoryResponse[].class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "getByCategory", e);
		}

		return (recipes == null || recipes.length == 0) ? null : Arrays.asList(recipes);
	}

	public RecipeDto save(RecipeDto recipeDto, String jwtToken) {
		RecipeDto newRecipeDto = null;

		JSONObject newRecipeJsonObject = new JSONObject();
		try {
			newRecipeJsonObject.put("name", recipeDto.getName());
			newRecipeJsonObject.put("vote", recipeDto.getDescription());
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

	public RecipesUpdateRequest update(Long id, String token, RecipesUpdateRequest recipeToUpdate) {
		RecipesUpdateRequest recipe = null;

		JSONObject updateRecipeJsonObject = new JSONObject();
		try {
			updateRecipeJsonObject.put("id", recipeToUpdate.getId());
			updateRecipeJsonObject.put("name", recipeToUpdate.getName());
			updateRecipeJsonObject.put("description", recipeToUpdate.getDescription());
			updateRecipeJsonObject.put("category", recipeToUpdate.getCategory().name());

			JSONArray productsJsonArray = new JSONArray();
			for (ProductDto product : recipeToUpdate.getProducts()) {
				JSONObject productJson = new JSONObject();
				productJson.put("id", product.getId());
				productJson.put("name", product.getName());
				productJson.put("measure", product.getMeasure());
				productJson.put("qty", product.getQty());
				productsJsonArray.put(productJson);
			}
			updateRecipeJsonObject.put("products", productsJsonArray);

			JSONArray votesJsonArray = new JSONArray();
			for (VoteDto vote : recipeToUpdate.getVotes()) {
				JSONObject voteJson = new JSONObject();
				voteJson.put("id", vote.getId());
				voteJson.put("rating", vote.getRating());
				votesJsonArray.put(voteJson);
			}
			updateRecipeJsonObject.put("votes", votesJsonArray);

		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(updateRecipeJsonObject.toString(), headers);

		ResponseEntity<RecipesUpdateRequest> responseEntity = restTemplate.exchange(
				RECIPES_SERVICE_URL + "/api/v1/recipes/" + id,
				HttpMethod.PUT,
				requestEntity,
				RecipesUpdateRequest.class
		);

		recipe = responseEntity.getBody();

		return recipe;
	}
}
