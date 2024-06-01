package org.example.util;



import org.example.dto.CategoryDto;
import org.example.dto.ProductDto;
import org.example.dto.RecipeDto;
import org.example.dto.VoteDto;
import org.example.model.Category;
import org.example.model.Product;
import org.example.model.Recipe;
import org.example.model.Vote;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RecipeMapper {
    public static RecipeDto convertRecipeToRecipeDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .rating(recipe.getRating())
                .countVotes(recipe.getCountVotes())
                .category(CategoryDto.fromCategory(recipe.getCategory()))
                .products(RecipeMapper.convertProductListToProductDtoList(recipe.getProducts()))
                .votes(RecipeMapper.convertVoteListToVoteDtoList(recipe.getVotes()))
                .build();
    }

    public static ProductDto convertProductToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .recipeId(product.getRecipeId())
                .name(product.getName())
                .qty(product.getQty())
                .measure(product.getMeasure())
                .build();
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
        return VoteDto.builder()
                .id(vote.getId())
                .recipeId(vote.getRecipeId())
                .rating(vote.getRating())
                .build();
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
        return Recipe.builder()
                .id(recipeDto.getId())
                .name(recipeDto.getName())
                .description(recipeDto.getDescription())
                .rating(recipeDto.getRating())
                .countVotes(recipeDto.getCountVotes())
                .category(Category.fromCategoryDto(recipeDto.getCategory()))
                .products(RecipeMapper.convertProductDtoListToProductList(recipeDto.getProducts()))
                .votes(RecipeMapper.convertVoteDtoListToVoteList(recipeDto.getVotes()))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Product convertProductDtoToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .recipeId(productDto.getRecipeId())
                .name(productDto.getName())
                .qty(productDto.getQty())
                .measure(productDto.getMeasure())
                .build();
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
        vote.setRecipeId(voteDto.getRecipeId());
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
