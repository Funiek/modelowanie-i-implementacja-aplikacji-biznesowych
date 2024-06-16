package org.example.repository;

import org.example.model.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long> {

    /**
     * Finds all products by recipe ID.
     *
     * @param recipeId the ID of the recipe
     * @return a flux of products that match the given recipe ID
     */
    Flux<Product> findAllByRecipeId(Long recipeId);

    /**
     * Deletes all products by recipe ID.
     *
     * @param recipeId the ID of the recipe
     * @return a mono indicating completion of the delete operation
     */
    Mono<Void> deleteAllByRecipeId(Long recipeId);
}