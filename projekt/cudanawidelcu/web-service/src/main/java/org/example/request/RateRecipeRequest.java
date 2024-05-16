package pl.cudanawidelcu.microservices.services.web.Request;

import lombok.Data;

@Data
public class RateRecipeRequest {
    private String name;
    private int vote;
}
