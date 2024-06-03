package org.example.service;

import org.example.model.Product;
import org.example.model.Recipe;
import org.example.model.Vote;
import org.example.repository.RecipeRepository;
import org.example.util.RecipeMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final ProductService productService;
    private final VoteService voteService;

    public RecipeServiceImpl(RecipeRepository recipeRepository, ProductService productService, VoteService voteService) {
        this.recipeRepository = recipeRepository;
        this.productService = productService;
        this.voteService = voteService;
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
                )
                .flatMap(recipe -> voteService.findAllByRecipe(recipe.getId())
                        .collectList()
                        .map(voteDtos -> {
                            List<Vote> votes = voteDtos.stream()
                                    .map(RecipeMapper::convertVoteDtoToVote)
                                    .collect(Collectors.toList());
                            recipe.setVotes(votes);
                            return recipe;
                        })
                );
    }

}
