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
        productDto.setQty(product.getQty());
        productDto.setMeasure(product.getMeasure());

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
        voteDto.setRecipe(RecipeMapper.convertRecipeToRecipeDto(vote.getRecipe()));

        return voteDto;
    }

    public static List<VoteDto> convertVoteListToVoteDtoList(List<Vote> voteList) {
        List<VoteDto> voteListDto = new ArrayList<>();
        for (Vote vote : voteList) {
            voteListDto.add(RecipeMapper.convertVoteToVoteDto(vote));
        }
        return voteListDto;
    }

    public static Recipe convertRecipeDtoToRecipe(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setRating(recipeDto.getRating());
        recipe.setCountVotes(recipeDto.getCountVotes());
        recipe.setCategory(recipeDto.getCategory());
        recipe.setProducts(RecipeMapper.convertProductDtoListToProductList(recipeDto.getProducts()));
        recipe.setVotes(RecipeMapper.convertVoteDtoListToVoteList(recipeDto.getVotes()));

        return recipe;
    }

    public static Product convertProductDtoToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setQty(productDto.getQty());
        product.setMeasure(productDto.getMeasure());

        return product;
    }

    public static List<Product> convertProductDtoListToProductList(List<ProductDto> productDtoList) {
        List<Product> productList = new ArrayList<>();
        for (ProductDto productDto : productDtoList) {
            productList.add(RecipeMapper.convertProductDtoToProduct(productDto));
        }
        return productList;
    }

    public static Vote convertVoteDtoToVote(VoteDto voteDto) {
        Vote vote = new Vote();
        vote.setRating(voteDto.getRating());
        vote.setRecipe(RecipeMapper.convertRecipeDtoToRecipe(voteDto.getRecipe()));
        return vote;
    }

    public static List<Vote> convertVoteDtoListToVoteList(List<VoteDto> voteDtoList) {
        List<Vote> voteList = new ArrayList<>();
        for (VoteDto voteDto : voteDtoList) {
            voteList.add(RecipeMapper.convertVoteDtoToVote(voteDto));
        }
        return voteList;
    }
}
