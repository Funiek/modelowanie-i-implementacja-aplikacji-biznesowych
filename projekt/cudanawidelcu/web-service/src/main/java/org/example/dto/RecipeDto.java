package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDto {
    private Long id;
    private String name;
    private String description;
    private Double rating;
    private int countVotes;
    private CategoryDto category;
    private List<ProductDto> products;
    private List<VoteDto> votes;
}
