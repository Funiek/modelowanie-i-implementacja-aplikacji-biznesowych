package org.example.util;

import org.example.dto.ProductDto;
import org.example.model.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    public static ProductDto convertProductToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .recipeId(product.getRecipeId())
                .name(product.getName())
                .qty(product.getQty())
                .measure(product.getMeasure())
                .build();
    }

    public static List<ProductDto> convertProductListToProductDtoList(List<Product> productList) {
        List<ProductDto> productListDto = new ArrayList<>();

        if (productList == null) {
            return productListDto;
        }

        for (Product product : productList) {
            productListDto.add(ProductMapper.convertProductToProductDto(product));
        }
        return productListDto;
    }

    public static Product convertProductDtoToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .recipeId(productDto.getRecipeId())
                .name(productDto.getName())
                .qty(productDto.getQty())
                .measure(productDto.getMeasure())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static List<Product> convertProductDtoListToProductList(List<ProductDto> productDtoList) {
        List<Product> productList = new ArrayList<>();
        if (productDtoList == null) {
            return productList;
        }

        for (ProductDto productDto : productDtoList) {
            productList.add(ProductMapper.convertProductDtoToProduct(productDto));
        }
        return productList;
    }
}
