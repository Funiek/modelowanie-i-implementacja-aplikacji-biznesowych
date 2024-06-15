package org.example.service;

import org.example.dto.CategoryDto;
import org.example.dto.RecipeDto;
import org.example.request.RecipesUpdateRequest;
import org.example.response.*;

import java.util.List;

public interface RecipesService {

    List<RecipesFindAllResponse> findAll();

    List<RecipesFindAllByCategoryResponse> findAllByCategory(CategoryDto categoryDto);

    RecipeDto save(RecipeDto recipeDto, String jwtToken);

    RecipesFindByIdResponse findById(Long id);

    void delete(Long id, String token);

    RecipesUpdateResponse update(Long id, String token, RecipesUpdateRequest recipeToUpdate);
}