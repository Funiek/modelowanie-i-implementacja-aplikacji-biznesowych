package org.example.dto;

import lombok.Data;
import org.example.model.Category;

import java.util.List;

@Data
public class RecipeDto {
    private String name;
    private String description;
    private Double rating;
    private int countVotes;
    private Category category;
    private List<ProductDto> products;
    private List<VoteDto> votes;
}
