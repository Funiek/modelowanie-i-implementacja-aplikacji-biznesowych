package pl.cudanawidelcu.microservices.recipes.dto;

import lombok.Data;
import pl.cudanawidelcu.microservices.recipes.model.Category;
import pl.cudanawidelcu.microservices.recipes.model.Product;
import pl.cudanawidelcu.microservices.recipes.model.Vote;

import javax.persistence.*;
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
