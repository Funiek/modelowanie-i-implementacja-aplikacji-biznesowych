package org.example.controller;

import org.example.dto.ProductDto;
import org.example.model.Product;
import org.example.service.ProductService;
import org.example.util.ProductMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;

@RestController
@EnableWebFlux
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<ProductDto> findAll() {
        Flux<Product> products = productService.findAll();
        // TODO przy maperach zweryfikować czy lepiej jest korzystać z product.map czy całość zamknąć w convertProductListToProductDtoList.
        return products.map(ProductMapper::convertProductToProductDto);
    }
}
