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

import java.util.ArrayList;
import java.util.List;
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
    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipeByName(String name) {
        return recipeRepository.findFirstByName(name).get(0);
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe updateRecipe(String name, Recipe recipe) throws RecipeNotFoundException {
        List<Recipe> potentialRecipes = recipeRepository.findFirstByName(name);
        if (!potentialRecipes.isEmpty()) {
            Recipe existingRecipe = potentialRecipes.get(0);
            BeanUtils.copyProperties(recipe, existingRecipe, "id", "votes", "products");

            return recipeRepository.save(existingRecipe);
        }

        throw new RecipeNotFoundException(name);
    }

    @Override
    @Transactional
    public Recipe rateRecipe(String name, int vote) throws RecipeNotFoundException {
        Recipe recipe = null;
        try {
            recipe = recipeRepository.findFirstByName(name).get(0);
            recipe = recipeRepository.findById(recipe.getId()).orElse(null);
        }
        catch (Exception e) {
            throw new RecipeNotFoundException(name);
        }

        if(recipe == null) return null;

        int countVotes = recipe.getCountVotes() + 1;
        recipe.setCountVotes(countVotes);

        List<Vote> votes = recipe.getVotes();
        if (votes == null) votes = new ArrayList<>();

        Vote newVote = new Vote();
        newVote.setRating((double) vote);
        newVote.setRecipe(recipe);
        votes.add(newVote);

        recipe.setVotes(votes);

        Double newRating = 0.0;
        for(Vote v: recipe.getVotes()) {
            newRating += v.getRating();
        }
        newRating = newRating / countVotes;

        recipe.setRating(newRating);
        recipeRepository.save(recipe);

        return recipe;
    }

    @Override
    public List<Recipe> getRecipesByCategory(Category category) {
        return recipeRepository.findRecipesByCategory(category);
    }

    @Override
    public void deleteRecipeByName(String name) {
        recipeRepository.deleteByName(name);
    }
}