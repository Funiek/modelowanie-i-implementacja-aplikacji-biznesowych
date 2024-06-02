package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@Data
@Builder
public class RecipeDto {
    private Long id;
    private String name;
    private String description;
    private Double rating;
    private int countVotes;
    private CategoryDto category;
    private List<ProductDto> products;
    private Flux<VoteDto> votes;
}
