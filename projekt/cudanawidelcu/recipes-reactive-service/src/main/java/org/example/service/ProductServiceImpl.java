package org.example.service;

import org.example.dto.ProductDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final WebClient.Builder builder;
    private final String PRODUCTS_SERVICE_URL = "http://APPLICATION-GATEWAY/products-service";

    public ProductServiceImpl(WebClient.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Flux<ProductDto> findAll() {
        return builder.build()
                .get()
                .uri(PRODUCTS_SERVICE_URL + "/api/v1/products")
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }

    @Override
    public Flux<ProductDto> findAllByRecipe(Long recipeId) {
        return builder.build()
                .get()
                .uri(PRODUCTS_SERVICE_URL + "/api/v1/products/recipe/" + recipeId)
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }

    @Override
    public Mono<ProductDto> save(ProductDto productDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return builder.build()
                .post()
                .uri(PRODUCTS_SERVICE_URL + "/api/v1/products")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(productDto, ProductDto.class)
                .retrieve()
                .bodyToMono(ProductDto.class);
    }

    @Override
    public Mono<Void> deleteAllByRecipeId(Long recipeId) {
        return builder.build()
                .delete()
                .uri(PRODUCTS_SERVICE_URL + "/api/v1/products" + recipeId)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
