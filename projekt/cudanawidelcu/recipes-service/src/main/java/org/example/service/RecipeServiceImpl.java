package org.example.service;


import org.example.exceptions.RecipeNotFoundException;
import org.example.model.Category;
import org.example.model.Recipe;
import org.example.model.Vote;
import org.example.repository.ProductRepository;
import org.example.repository.RecipeRepository;
import org.example.repository.VoteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class RecipeServiceImpl implements RecipeService {
    final RecipeRepository recipeRepository;
    final ProductRepository productRepository;
    final VoteRepository voteRepository;

    protected Logger logger = Logger.getLogger(RecipeServiceImpl.class.getName());

    public RecipeServiceImpl(RecipeRepository recipeRepository, ProductRepository productRepository, VoteRepository voteRepository) {
        this.recipeRepository = recipeRepository;
        this.productRepository = productRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Mono<Recipe> getRecipe(Long id) {
        return recipeRepository.findById(id);
    }

    @Override
    public Mono<Recipe> createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Mono<Recipe> updateRecipe(String name, Recipe recipe) throws RecipeNotFoundException {
        return recipeRepository.existsById(recipe.getId())
                .flatMap(exists -> {
                    if (exists) {
                        return recipeRepository.findById(recipe.getId())
                                .flatMap(existingRecipe -> {
                                    BeanUtils.copyProperties(recipe, existingRecipe, "id", "votes", "products");
                                    return recipeRepository.save(existingRecipe);
                                });
                    }
                    else {
                        return Mono.error(new RecipeNotFoundException(name));
                    }
                });
    }

    @Override
    @Transactional
    public Mono<Recipe> rateRecipe(Long id, int vote) throws NullPointerException {
        return recipeRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return recipeRepository.findById(id)
                                .flatMap(recipe -> {
                                    int countVotes = recipe.getCountVotes() + 1;
                                    recipe.setCountVotes(countVotes);

                                    Vote newVote = new Vote();
                                    newVote.setRating((double) vote);
                                    newVote.setRecipe(recipe);

                                    return voteRepository.save(newVote)
                                            .flatMap(savedVote -> {
                                                List<Vote> votes = recipe.getVotes();
                                                if (votes == null) {
                                                    votes = new ArrayList<>();
                                                }
                                                votes.add(savedVote);
                                                recipe.setVotes(votes);

                                                Double newRating = 0.0;

                                                for(Vote v: recipe.getVotes()) {
                                                    newRating += v.getRating();
                                                }
                                                newRating = newRating / countVotes;

                                                recipe.setRating(newRating);

                                                return recipeRepository.save(recipe);
                                            });
                                });
                    }
                    else {
                        return Mono.error(new NullPointerException());
                    }
                });
//        Recipe recipe = null;
//        try {
//            recipe = recipeRepository.findById(id).orElse(null);
//        }
//        catch (Exception e) {
//            throw new NullPointerException();
//        }
//
//        if(recipe == null) return null;
//
//        int countVotes = recipe.getCountVotes() + 1;
//        recipe.setCountVotes(countVotes);
//
//        List<Vote> votes = recipe.getVotes();
//        if (votes == null) votes = new ArrayList<>();
//
//        Vote newVote = new Vote();
//        newVote.setRating((double) vote);
//        newVote.setRecipe(recipe);
//        voteRepository.save(newVote);
//
//        votes.add(newVote);
//
//        recipe.setVotes(votes);
//
//        Double newRating = 0.0;
//        for(Vote v: recipe.getVotes()) {
//            newRating += v.getRating();
//        }
//        newRating = newRating / countVotes;
//
//        recipe.setRating(newRating);
//        recipeRepository.save(recipe);
//
//        return recipe;
    }

//    @Override
//    public List<Recipe> getRecipesByCategory(Category category) {
//        return recipeRepository.findRecipesByCategory(category);
//    }

    @Override
    public Mono<Void> deleteRecipe(Long id) {
        return recipeRepository.deleteById(id);
    }
}
