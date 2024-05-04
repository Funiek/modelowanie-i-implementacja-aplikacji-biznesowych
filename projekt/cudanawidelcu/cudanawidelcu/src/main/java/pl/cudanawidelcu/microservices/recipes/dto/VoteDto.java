package pl.cudanawidelcu.microservices.recipes.dto;

import lombok.Data;
import pl.cudanawidelcu.microservices.recipes.model.Recipe;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class VoteDto {
    private Double rating;
    private Recipe recipe;
}
