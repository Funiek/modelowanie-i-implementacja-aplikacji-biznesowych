package org.example.service;

import org.example.dto.ProductDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Implementation of ProductService that interacts with a remote products service via WebClient.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final WebClient.Builder builder;
    private final String PRODUCTS_SERVICE_URL = "http://APPLICATION-GATEWAY/products-service";

    public ProductServiceImpl(WebClient.Builder builder) {
        this.builder = builder;
    }

    /**
     * Retrieves all products from the remote products service.
     *
     * @return a Flux emitting all products
     */
    @Override
    public Flux<ProductDto> findAll() {
        return builder.build()
                .get()
                .uri(PRODUCTS_SERVICE_URL + "/api/v1/products")
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }

    /**
     * Retrieves all products from the remote products service associated with a specific recipe.
     *
     * @param recipeId the ID of the recipe
     * @return a Flux emitting all products associated with the recipe
     */
    @Override
    public Flux<ProductDto> findAllByRecipe(Long recipeId) {
        return builder.build()
                .get()
                .uri(PRODUCTS_SERVICE_URL + "/api/v1/products/recipe/" + recipeId)
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }

    /**
     * Saves a new product using the remote products service.
     *
     * @param productDto the product to save
     * @return a Mono emitting the saved product
     */
    @Override
    public Mono<ProductDto> save(ProductDto productDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return builder.build()
                .post()
                .uri(PRODUCTS_SERVICE_URL + "/api/v1/products")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(Mono.just(productDto), ProductDto.class)
                .retrieve()
                .bodyToMono(ProductDto.class);
    }

    /**
     * Deletes all products associated with a specific recipe using the remote products service.
     *
     * @param recipeId the ID of the recipe
     * @return a Mono emitting when the deletion is complete
     */
    @Override
    public Mono<Void> deleteAllByRecipeId(Long recipeId) {
        return builder.build()
                .delete()
                .uri(PRODUCTS_SERVICE_URL + "/api/v1/products/recipe/" + recipeId)
                .retrieve()
                .bodyToMono(Void.class);
    }
}