package org.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "vote")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double rating;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private Recipe recipe;
}
