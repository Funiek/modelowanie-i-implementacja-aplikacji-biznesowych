package pl.cudanawidelcu.microservices.services.web.dto;

import lombok.Data;
import pl.cudanawidelcu.microservices.recipes.model.Recipe;

@Data
public class VoteDto {
    private Double rating;
    private Recipe recipe;
}
