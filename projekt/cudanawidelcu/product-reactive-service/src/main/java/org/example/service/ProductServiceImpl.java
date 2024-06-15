package org.example.service;

import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Flux<Product> findAllByRecipe(Long recipeId) {
        return productRepository.findAllByRecipeId(recipeId);
    }

    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Flux<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public Mono<Void> deleteAllByRecipeId(Long recipeId) {
        return productRepository.deleteAllByRecipeId(recipeId);
    }


}
