package org.example.request;

import lombok.Data;
import org.example.dto.CategoryDto;
import org.example.dto.ProductDto;
import org.example.dto.VoteDto;

import java.util.List;

@Data
public class CreateRecipeRequest {
    private Long id;
    private String name;
    private String description;
    private Double rating;
    private int countVotes;
    private CategoryDto category;
    private List<ProductDto> products;
    private List<VoteDto> votes;
    private byte[] image;
}
