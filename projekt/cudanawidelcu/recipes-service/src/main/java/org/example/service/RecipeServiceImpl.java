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
    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe updateRecipe(String name, Recipe recipe) throws RecipeNotFoundException {
        Recipe existingRecipe = recipeRepository.findById(recipe.getId()).orElseThrow(() -> new RecipeNotFoundException(name));
        BeanUtils.copyProperties(recipe, existingRecipe, "id", "votes", "products");
        return recipeRepository.save(existingRecipe);
    }

    @Override
    @Transactional
    public Recipe rateRecipe(Long id, int vote) throws NullPointerException {
        Recipe recipe = null;
        try {
            recipe = recipeRepository.findById(id).orElse(null);
        }
        catch (Exception e) {
            throw new NullPointerException();
        }

        if(recipe == null) return null;

        int countVotes = recipe.getCountVotes() + 1;
        recipe.setCountVotes(countVotes);

        List<Vote> votes = recipe.getVotes();
        if (votes == null) votes = new ArrayList<>();

        Vote newVote = new Vote();
        newVote.setRating((double) vote);
        newVote.setRecipe(recipe);
        voteRepository.save(newVote);

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

//    @Override
//    public List<Recipe> getRecipesByCategory(Category category) {
//        return recipeRepository.findRecipesByCategory(category);
//    }

    @Override
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}
