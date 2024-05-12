package pl.cudanawidelcu.microservices.recipes.dto;

import lombok.Data;

@Data
public class VoteDto {
    private Double rating;
    private RecipeDto recipe;
}
