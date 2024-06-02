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
import java.util.stream.Collectors;

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
        return recipeRepository.findAll()
                .flatMap(recipe -> productService.findAllByRecipe(recipe.getId())
                        .collectList()
                        .map(productDtos -> {
                            List<Product> products = productDtos.stream()
                                    .map(RecipeMapper::convertProductDtoToProduct)
                                    .collect(Collectors.toList());
                            recipe.setProducts(products);
                            return recipe;
                        })
                );
    }

}
