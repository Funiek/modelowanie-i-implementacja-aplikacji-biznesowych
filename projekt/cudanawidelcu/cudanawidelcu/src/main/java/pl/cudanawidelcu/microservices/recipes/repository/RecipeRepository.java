package pl.cudanawidelcu.microservices.recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.cudanawidelcu.microservices.recipes.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long>  {
}
