package org.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table(name = "vote")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vote {
    @Id
    private Long id;
    private Double rating;
    private Date createdAt;
}
