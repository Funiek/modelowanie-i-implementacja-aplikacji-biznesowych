package pl.cudanawidelcu.microservices.recipes.util;

import pl.cudanawidelcu.microservices.recipes.dto.ProductDto;
import pl.cudanawidelcu.microservices.recipes.dto.RecipeDto;
import pl.cudanawidelcu.microservices.recipes.dto.RoleDto;
import pl.cudanawidelcu.microservices.recipes.dto.VoteDto;
import pl.cudanawidelcu.microservices.recipes.model.Product;
import pl.cudanawidelcu.microservices.recipes.model.Recipe;
import pl.cudanawidelcu.microservices.recipes.model.Role;
import pl.cudanawidelcu.microservices.recipes.model.Vote;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeMapper {
    public static RecipeDto convertRecipeToRecipeDto(Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName(recipe.getName());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setRating(recipe.getRating());
        recipeDto.setCountVotes(recipe.getCountVotes());
        recipeDto.setCategory(recipe.getCategory());
        recipeDto.setProducts(RecipeMapper.convertProductListToProductDtoList(recipe.getProducts()));
        recipeDto.setVotes(RecipeMapper.convertVoteListToVoteDtoList(recipe.getVotes()));

        return recipeDto;
    }

    public static ProductDto convertProductToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setQty(productDto.getQty());
        productDto.setMeasure(productDto.getMeasure());

        return productDto;
    }

    public static List<ProductDto> convertProductListToProductDtoList(List<Product> productList) {
        List<ProductDto> productListDto = new ArrayList<>();
        for (Product product : productList) {
            productListDto.add(RecipeMapper.convertProductToProductDto(product));
        }
        return productListDto;
    }

    public static VoteDto convertVoteToVoteDto(Vote vote) {
        VoteDto voteDto = new VoteDto();
        voteDto.setRating(vote.getRating());
        voteDto.setRecipe(vote.getRecipe());

        return voteDto;
    }

    public static List<VoteDto> convertVoteListToVoteDtoList(List<Vote> voteList) {
        List<VoteDto> voteListDto = new ArrayList<>();
        for (Vote vote : voteList) {
            voteListDto.add(RecipeMapper.convertVoteToVoteDto(vote));
        }
        return voteListDto;
    }
}
