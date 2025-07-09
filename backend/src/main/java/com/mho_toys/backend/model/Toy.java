package com.mho_toys.backend.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;


@Entity
@Table(name = "toys")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Toy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long toyId;


    @NotBlank( message = "Toy name cannot be blank")
    @Size(min = 3, max = 25, message = "Toy name must be between 3 and 25 characters")

    private String name;

    @NotBlank(message = "Toy description cannot be blank")
    @Size(min = 5, max = 100, message = "Toy description must be between 5 and 100 characters")
    private String description;

    @NotNull(message = "Toy price cannot be null")
    private Double price;

    @NotNull(message = "Toy age group cannot be null")
    @Min(value = 1, message = "Toy age group must be at least 1")
    @Max(value = 10, message = "Toy age group must be at most 10")
    private int ageRange;

    private String imageUrl;

    private Instant createdAt;
    private String createdBy;

    private Instant updatedAt;
    private String updatedBy;
}
