package org.example.controller;

import org.example.dto.ProductDto;
import org.example.model.Product;
import org.example.service.ProductService;
import org.example.util.ProductMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@EnableWebFlux
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<ProductDto> findAll() {
        Flux<Product> products = productService.findAll();
        // TODO czy lepiej nie będzie zamienić na flatmap jeśli się da?
        return products.map(ProductMapper::convertProductToProductDto);
    }

    @GetMapping("/recipe/{recipeId}")
    public Flux<ProductDto> findAllByRecipeId(@PathVariable("recipeId") Long recipeId) {
        Flux<Product> products = productService.findAllByRecipe(recipeId);
        return products.map(ProductMapper::convertProductToProductDto);
    }

    @PostMapping
    public Mono<ProductDto> save(@RequestBody ProductDto productDto) {
        // TODO pewnie do poprawienia żeby przyjmował parametr w MONO
        Product product = ProductMapper.convertProductDtoToProduct(productDto);
        Mono<Product> createProduct = productService.save(product);
        return createProduct.map(ProductMapper::convertProductToProductDto);
    }

    @DeleteMapping("/recipe/{recipeId}")
    public Mono<Void> deleteByRecipeId(@PathVariable("recipeId") Long recipeId) {
        return productService.deleteAllByRecipeId(recipeId);
    }
}

