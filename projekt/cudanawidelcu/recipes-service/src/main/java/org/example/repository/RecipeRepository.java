package org.example.repository;

import org.example.model.Recipe;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface RecipeRepository extends R2dbcRepository<Recipe, Long> {
}
