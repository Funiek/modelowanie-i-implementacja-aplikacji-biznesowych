package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VoteDto {
    private Long id;
    private Long recipeId;
    private Double rating;
    private LocalDateTime createdAt;
}
