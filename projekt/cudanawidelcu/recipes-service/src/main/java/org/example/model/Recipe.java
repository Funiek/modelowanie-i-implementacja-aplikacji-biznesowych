package org.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double rating;
    private int countVotes;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "recipe_product",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy="recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Vote> votes = new ArrayList<>();
}
