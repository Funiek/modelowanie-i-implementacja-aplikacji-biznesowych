package org.example.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductsSaveBatchResponse {
    private Long id;
    private Long recipeId;
    private String name;
    private String measure;
    private Double qty;
}
