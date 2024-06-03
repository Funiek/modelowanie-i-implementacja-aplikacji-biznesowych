package org.example.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vote {
    private Long id;
    private Long recipeId;
    private Double rating;
    private LocalDateTime createdAt;
}
