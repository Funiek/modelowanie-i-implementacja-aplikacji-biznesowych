package org.example.service;

import org.example.dto.ProductDto;
import org.example.model.Product;
import org.example.model.Recipe;
import org.example.repository.RecipeRepository;
import org.example.util.RecipeMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final ProductService productService;

    public RecipeServiceImpl(RecipeRepository recipeRepository, ProductService productService) {
        this.recipeRepository = recipeRepository;
        this.productService = productService;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        Flux<Recipe> recipeFlux = recipeRepository.findAll();
        return recipeFlux.flatMap(recipe -> {
            Flux<ProductDto> productDtoFlux = productService.findAllByRecipe(recipe.getId());
            Flux<Product> productFlux = productDtoFlux.map(RecipeMapper::convertProductDtoToProduct);
            recipe.setProducts(productFlux);
            // TODO tu chyba trzeba zmienić return na coś innego. Pamiętać żeby poszukać
            return Mono.just(recipe);
        });
    }
}
