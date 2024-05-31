package org.example.service;

import org.example.model.Recipe;
import org.example.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }
}
