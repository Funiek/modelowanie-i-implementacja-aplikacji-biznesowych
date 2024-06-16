package org.example.controller;

import org.example.dto.ProductDto;
import org.example.model.Product;
import org.example.service.ProductService;
import org.example.util.ProductMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Controller class for managing product-related HTTP requests.
 */
@RestController
@EnableWebFlux
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductController {

    private final ProductService productService;

    /**
     * Constructor for ProductController.
     *
     * @param productService ProductService instance
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products.
     *
     * @return Flux of ProductDto
     */
    @GetMapping
    public Flux<ProductDto> findAll() {
        Flux<Product> products = productService.findAll();
        return products.map(ProductMapper::convertProductToProductDto);
    }

    /**
     * Retrieves all products by recipe ID.
     *
     * @param recipeId recipe ID
     * @return Flux of ProductDto
     */
    @GetMapping("/recipe/{recipeId}")
    public Flux<ProductDto> findAllByRecipeId(@PathVariable("recipeId") Long recipeId) {
        Flux<Product> products = productService.findAllByRecipe(recipeId);
        return products.map(ProductMapper::convertProductToProductDto);
    }

    /**
     * Saves a product.
     *
     * @param productDto ProductDto object to save
     * @return Mono of ProductDto
     */
    @PostMapping
    public Mono<ProductDto> save(@RequestBody ProductDto productDto) {
        Product product = ProductMapper.convertProductDtoToProduct(productDto);
        Mono<Product> createProduct = productService.save(product);
        return createProduct.map(ProductMapper::convertProductToProductDto);
    }

    /**
     * Saves a batch of products.
     *
     * @param productDtos list of ProductDto objects to save
     * @return Flux of ProductDto
     */
    @PostMapping("/batch")
    public Flux<ProductDto> saveAll(@RequestBody List<ProductDto> productDtos) {
        List<Product> products = productDtos.stream()
                .map(ProductMapper::convertProductDtoToProduct)
                .toList();

        Flux<Product> createProducts = productService.saveAll(products);

        return createProducts.map(ProductMapper::convertProductToProductDto);
    }

    /**
     * Deletes all products by recipe ID.
     *
     * @param recipeId recipe ID
     * @return Mono<Void>
     */
    @DeleteMapping("/recipe/{recipeId}")
    public Mono<Void> deleteByRecipeId(@PathVariable("recipeId") Long recipeId) {
        return productService.deleteAllByRecipeId(recipeId);
    }
}