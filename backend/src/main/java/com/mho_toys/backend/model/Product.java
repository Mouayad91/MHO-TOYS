package com.mho_toys.backend.model;


import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;


    @NotBlank( message = "Toy name cannot be blank")
    @Size(min = 3, max = 25, message = "Toy name must be between 3 and 25 characters")

    private String name;

    @NotBlank(message = "Toy description cannot be blank")
    @Size(min = 5, max = 200, message = "Toy description must be between 5 and 200 characters")
    private String description;

    @NotNull(message = "Toy price cannot be null")
    private Double price;

    @NotNull(message = "Toy age group cannot be null")
    @Column(name = "age_range")
    @Pattern(
        regexp = "^(\\d+(-\\d+)?\\s+(Months?|Years?)|\\d+\\+\\s+(Months?|Years?))$",
        message = "Age range must follow format: '6-12 Months', '2-3 Years', '5+ Years', etc."
    )
    private String ageRange;

    private String imageUrl;

    @CreatedDate
    private Instant createdAt;
    private String createdBy;

    @LastModifiedDate
    private Instant updatedAt;
    private String updatedBy;
}
