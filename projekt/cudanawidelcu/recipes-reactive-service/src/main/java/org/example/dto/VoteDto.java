package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    private Long id;
    private Long recipeId;
    private Double rating;
    private LocalDateTime createdAt;
}
