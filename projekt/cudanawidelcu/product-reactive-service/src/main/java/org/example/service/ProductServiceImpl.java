package org.example.service;

import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service implementation for managing products.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /**
     * Constructs a new instance of ProductService.
     *
     * @param productRepository the repository for product operations
     */
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all products.
     *
     * @return a flux of all products
     */
    @Override
    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Retrieves all products by recipe ID.
     *
     * @param recipeId the ID of the recipe
     * @return a flux of products that match the given recipe ID
     */
    @Override
    public Flux<Product> findAllByRecipe(Long recipeId) {
        return productRepository.findAllByRecipeId(recipeId);
    }

    /**
     * Saves a product.
     *
     * @param product the product to save
     * @return a mono of the saved product
     */
    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }

    /**
     * Saves a list of products.
     *
     * @param products the list of products to save
     * @return a flux of the saved products
     */
    @Override
    public Flux<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    /**
     * Deletes all products by recipe ID.
     *
     * @param recipeId the ID of the recipe
     * @return a mono indicating completion of the delete operation
     */
    @Override
    public Mono<Void> deleteAllByRecipeId(Long recipeId) {
        return productRepository.deleteAllByRecipeId(recipeId);
    }

}