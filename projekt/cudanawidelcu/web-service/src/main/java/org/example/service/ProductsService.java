package org.example.service;

import org.example.dto.ProductDto;
import org.example.response.ProductsSaveBatchResponse;

import java.util.List;

public interface ProductsService {

    List<ProductsSaveBatchResponse> saveAll(List<ProductDto> productDtos, String jwtToken, Long recipeId);

}