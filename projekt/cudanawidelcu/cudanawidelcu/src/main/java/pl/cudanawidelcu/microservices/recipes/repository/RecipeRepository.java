package pl.cudanawidelcu.microservices.recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.cudanawidelcu.microservices.recipes.model.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>  {
    List<Recipe> findFirstByName(String name);
}
