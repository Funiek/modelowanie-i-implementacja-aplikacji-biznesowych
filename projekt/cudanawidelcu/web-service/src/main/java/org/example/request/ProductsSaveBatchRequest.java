package org.example.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductsSaveBatchRequest {
    private Long id;
    private Long recipeId;
    private String name;
    private String measure;
    private Double qty;
}
