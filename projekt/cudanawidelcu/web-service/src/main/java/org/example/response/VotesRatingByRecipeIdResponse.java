package org.example.response;

import lombok.Data;

@Data
public class VotesRatingByRecipeIdResponse {
    private Long recipeId;
    private Double rating;
    private int countVotes;
}
