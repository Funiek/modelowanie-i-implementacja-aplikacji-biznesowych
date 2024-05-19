package org.example.util;



import org.example.dto.CategoryDto;
import org.example.dto.ProductDto;
import org.example.dto.RecipeDto;
import org.example.dto.VoteDto;
import org.example.model.Category;
import org.example.model.Product;
import org.example.model.Recipe;
import org.example.model.Vote;

import java.util.ArrayList;
import java.util.List;

public class RecipeMapper {
    public static RecipeDto convertRecipeToRecipeDto(Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipe.getId());
        recipeDto.setName(recipe.getName());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setRating(recipe.getRating());
        recipeDto.setCountVotes(recipe.getCountVotes());
        recipeDto.setCategory(CategoryDto.fromCategory(recipe.getCategory()));
        recipeDto.setProducts(RecipeMapper.convertProductListToProductDtoList(recipe.getProducts()));
        recipeDto.setVotes(RecipeMapper.convertVoteListToVoteDtoList(recipe.getVotes()));

        return recipeDto;
    }

    public static ProductDto convertProductToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setQty(product.getQty());
        productDto.setMeasure(product.getMeasure());

        return productDto;
    }

    public static List<ProductDto> convertProductListToProductDtoList(List<Product> productList) {
        List<ProductDto> productListDto = new ArrayList<>();

        if (productList == null) {
            return productListDto;
        }

        for (Product product : productList) {
            productListDto.add(RecipeMapper.convertProductToProductDto(product));
        }
        return productListDto;
    }

    public static VoteDto convertVoteToVoteDto(Vote vote) {
        VoteDto voteDto = new VoteDto();
        voteDto.setId(vote.getId());
        voteDto.setRating(vote.getRating());

        return voteDto;
    }

    public static List<VoteDto> convertVoteListToVoteDtoList(List<Vote> voteList) {
        List<VoteDto> voteListDto = new ArrayList<>();
        if (voteList == null) {
            return voteListDto;
        }

        for (Vote vote : voteList) {
            voteListDto.add(RecipeMapper.convertVoteToVoteDto(vote));
        }
        return voteListDto;
    }

    public static Recipe convertRecipeDtoToRecipe(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeDto.getId());
        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setRating(recipeDto.getRating());
        recipe.setCountVotes(recipeDto.getCountVotes());
        recipe.setCategory(Category.fromCategoryDto(recipeDto.getCategory()));
        recipe.setProducts(RecipeMapper.convertProductDtoListToProductList(recipeDto.getProducts()));

        List<Vote> votes = RecipeMapper.convertVoteDtoListToVoteList(recipeDto.getVotes());
        votes.forEach(e -> e.setRecipe(recipe));
        recipe.setVotes(votes);

        return recipe;
    }

    public static Product convertProductDtoToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setQty(productDto.getQty());
        product.setMeasure(productDto.getMeasure());

        return product;
    }

    public static List<Product> convertProductDtoListToProductList(List<ProductDto> productDtoList) {
        List<Product> productList = new ArrayList<>();
        if (productDtoList == null) {
            return productList;
        }

        for (ProductDto productDto : productDtoList) {
            productList.add(RecipeMapper.convertProductDtoToProduct(productDto));
        }
        return productList;
    }

    public static Vote convertVoteDtoToVote(VoteDto voteDto) {
        Vote vote = new Vote();
        vote.setId(voteDto.getId());
        vote.setRating(voteDto.getRating());

        return vote;
    }

    public static List<Vote> convertVoteDtoListToVoteList(List<VoteDto> voteDtoList) {
        List<Vote> voteList = new ArrayList<>();
        if (voteDtoList == null) {
            return voteList;
        }

        for (VoteDto voteDto : voteDtoList) {
            voteList.add(RecipeMapper.convertVoteDtoToVote(voteDto));
        }
        return voteList;
    }
}
