package pl.cudanawidelcu.microservices.services.web.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private String measure;
    private Double qty;
}
