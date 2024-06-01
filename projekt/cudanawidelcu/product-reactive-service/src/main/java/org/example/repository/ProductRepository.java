package org.example.repository;

import org.example.model.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long> {
//    @Query("SELECT * FROM product p WHERE p.recipe_id = :recipeId")
    Flux<Product> findAllByRecipeId(Long recipeId);
    Mono<Void> deleteAllByRecipeId(Long recipeId);
}
