package org.example.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "recipe")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe {
    @Id
    private Long id;
    private String name;
    private String description;
    private Double rating;
    private int countVotes;
    private Category category;
    private LocalDateTime createdAt;

    @Transient
    private List<Product> products;
    @Transient
    private Flux<Vote> votes;
}
