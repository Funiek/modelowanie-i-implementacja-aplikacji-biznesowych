package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.dto.ProductDto;
import org.example.dto.RecipeDto;
import org.example.request.ProductsSaveBatchRequest;
import org.example.response.ProductsSaveBatchResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProductsService {
    private final RestTemplate restTemplate;
    private final String PRODUCTS_SERVICE_URL = "http://APPLICATION-GATEWAY/products-service";

    protected Logger logger = Logger.getLogger(RecipesService.class
            .getName());

    public ProductsService(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
    }




    public List<ProductsSaveBatchResponse> saveAll(List<ProductDto> productDtos, String jwtToken, Long recipeId) {
        ProductsSaveBatchResponse[] newProductSaveBatchResponse;

        JSONArray productsJsonArray = new JSONArray();
        for (ProductDto productDto : productDtos) {
            ProductsSaveBatchRequest productsSaveBatchRequest = ProductsSaveBatchRequest.builder()
                    .recipeId(recipeId)
                    .name(productDto.getName())
                    .measure(productDto.getMeasure())
                    .qty(productDto.getQty())
                    .build();

            JSONObject productJsonObject = new JSONObject();
            try {
                productJsonObject.put("name", productsSaveBatchRequest.getName());
                productJsonObject.put("measure", productsSaveBatchRequest.getMeasure());
                productJsonObject.put("qty", productsSaveBatchRequest.getQty());
                productJsonObject.put("recipeId", productsSaveBatchRequest.getRecipeId());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            productsJsonArray.put(productJsonObject);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(jwtToken);

            HttpEntity<String> requestEntity = new HttpEntity<>(productsJsonArray.toString(), headers);

            ResponseEntity<ProductsSaveBatchResponse[]> responseEntity = restTemplate.exchange(
                    PRODUCTS_SERVICE_URL + "/api/v1/products/batch",
                    HttpMethod.POST,
                    requestEntity,
                    ProductsSaveBatchResponse[].class
            );



            newProductSaveBatchResponse = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas wysyłania zapytania POST: " + e.getMessage(), e);
        }

        return (newProductSaveBatchResponse == null || newProductSaveBatchResponse.length == 0) ? new ArrayList<>() : Arrays.asList(newProductSaveBatchResponse);
    }

}
