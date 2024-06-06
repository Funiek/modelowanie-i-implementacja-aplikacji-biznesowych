package org.example.response;

import lombok.Data;
import org.example.dto.CategoryDto;
import org.example.dto.ProductDto;
import org.example.dto.VoteDto;

import java.util.List;

@Data
public class RecipesFindAllByCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private CategoryDto category;
    private List<ProductDto> products;
    private List<VoteDto> votes;
}
