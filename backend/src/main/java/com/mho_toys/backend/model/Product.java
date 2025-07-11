package com.mho_toys.backend.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;


@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;


    @NotBlank( message = "Toy name cannot be blank")
    @Size(min = 3, max = 25, message = "Toy name must be between 3 and 25 characters")

    private String name;

    @NotBlank(message = "Toy description cannot be blank")
    @Size(min = 5, max = 100, message = "Toy description must be between 5 and 100 characters")
    private String description;

    @NotNull(message = "Toy price cannot be null")
    private Double price;

    @NotNull(message = "Toy age group cannot be null")
    @Pattern(regexp = "^[0-9]+-[0-9]+$", message = "Age range must be in the format 'min-max'")
    private String ageRange;

    private String imageUrl;

    private Instant createdAt;
    private String createdBy;

    private Instant updatedAt;
    private String updatedBy;
}
