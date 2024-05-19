package org.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.Date;
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

    @Transient
    private List<Product> products = new ArrayList<>();
    private List<Vote> votes = new ArrayList<>();
    private Date createdAt;
}
