package org.example.response;

import lombok.Data;

@Data
public class VotesSaveResponse {
    private Long recipeId;
    private Double rating;
    private int countVotes;
}
