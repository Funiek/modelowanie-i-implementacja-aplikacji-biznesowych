package org.example.request;

import lombok.Data;

@Data
public class VotesSaveRequest {
    private Long recipeId;
    private int rating;
}
