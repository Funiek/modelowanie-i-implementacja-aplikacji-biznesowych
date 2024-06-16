package org.example.repository;

import org.example.model.Category;
import org.example.model.Recipe;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Repository interface for managing Recipe entities.
 */
@Repository
public interface RecipeRepository extends R2dbcRepository<Recipe, Long> {

    /**
     * Retrieves all recipes that belong to the specified category.
     *
     * @param category the category of recipes to retrieve
     * @return a Flux emitting all recipes in the specified category
     */
    Flux<Recipe> findAllByCategory(Category category);
}