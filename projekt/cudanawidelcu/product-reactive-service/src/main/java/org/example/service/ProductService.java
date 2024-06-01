package org.example.service;

import org.example.model.Product;
import reactor.core.publisher.Flux;

public interface ProductService {
    Flux<Product> findAll();
}
