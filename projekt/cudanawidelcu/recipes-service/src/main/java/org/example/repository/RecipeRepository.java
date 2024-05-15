package org.example.repository;

import org.example.model.Category;
import org.example.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findFirstByName(String name);
    List<Recipe> findRecipesByCategory(Category category);
    void deleteByName(String name);
}
