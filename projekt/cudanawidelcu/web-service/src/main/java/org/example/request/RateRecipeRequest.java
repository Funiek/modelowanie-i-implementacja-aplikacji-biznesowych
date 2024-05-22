package org.example.request;

import lombok.Data;

@Data
public class RateRecipeRequest {
    private Long id;
    private String name;
    private int vote;
}
