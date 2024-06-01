package org.example.service;

import org.example.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Flux<Product> findAll() {
        return null;
    }

    @Override
    public Flux<Product> findAllByRecipe(Long recipeId) {
        return null;
    }

    @Override
    public Mono<Product> save(Product product) {
        return null;
    }

    @Override
    public Mono<Void> deleteAllByRecipeId(Long recipeId) {
        return null;
    }
}
