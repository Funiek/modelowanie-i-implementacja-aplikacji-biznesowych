package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.dto.ProductDto;
import org.example.dto.RecipeDto;
import org.example.dto.VoteDto;
import org.example.request.RecipesFindAllRequest;
import org.example.request.VotesSaveRequest;
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

	public List<RecipesFindAllRequest> findAll() {
		RecipesFindAllRequest[] recipes = null;

		try {
			String url = RECIPES_SERVICE_URL + "/api/v1/recipes";
			recipes = restTemplate.getForObject(url, RecipesFindAllRequest[].class);
		}
		catch (HttpClientErrorException e) {
			logger.throwing(this.getClass().getSimpleName(), "getAll", e);
		}

		return (recipes == null || recipes.length == 0) ? null : Arrays.asList(recipes);
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

	public RecipeDto createRecipe(RecipeDto recipeDto, String jwtToken) {
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


	public RecipeDto rate(VotesSaveRequest votesSaveRequest) {
		RecipeDto recipeDto = null;

		JSONObject rateRecipeJsonObject = new JSONObject();
		try {
			rateRecipeJsonObject.put("recipeId", votesSaveRequest.getRecipeId());
			rateRecipeJsonObject.put("vote", votesSaveRequest.getVote());
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

	public RecipeDto update(Long id, String token, RecipeDto recipeDtoToUpdate) {
		RecipeDto recipeDto = null;

		JSONObject updateRecipeJsonObject = new JSONObject();
		try {
			updateRecipeJsonObject.put("id", recipeDtoToUpdate.getId());
			updateRecipeJsonObject.put("name", recipeDtoToUpdate.getName());
			updateRecipeJsonObject.put("description", recipeDtoToUpdate.getDescription());
			updateRecipeJsonObject.put("category", recipeDtoToUpdate.getCategory().name());

			JSONArray productsJsonArray = new JSONArray();
			for (ProductDto product : recipeDtoToUpdate.getProducts()) {
				JSONObject productJson = new JSONObject();
				productJson.put("id", product.getId());
				productJson.put("name", product.getName());
				productJson.put("measure", product.getMeasure());
				productJson.put("qty", product.getQty());
				productsJsonArray.put(productJson);
			}
			updateRecipeJsonObject.put("products", productsJsonArray);

			JSONArray votesJsonArray = new JSONArray();
			for (VoteDto vote : recipeDtoToUpdate.getVotes()) {
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

		ResponseEntity<RecipeDto> responseEntity = restTemplate.exchange(
				RECIPES_SERVICE_URL + "/api/v1/recipes/" + id,
				HttpMethod.PUT,
				requestEntity,
				RecipeDto.class
		);

		recipeDto = responseEntity.getBody();

		return recipeDto;
	}
}
