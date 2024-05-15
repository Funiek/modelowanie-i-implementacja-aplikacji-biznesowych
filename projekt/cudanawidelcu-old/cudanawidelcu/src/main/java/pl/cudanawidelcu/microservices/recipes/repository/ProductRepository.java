package pl.cudanawidelcu.microservices.recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.cudanawidelcu.microservices.recipes.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
