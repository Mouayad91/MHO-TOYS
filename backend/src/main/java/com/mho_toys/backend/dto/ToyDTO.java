package com.mho_toys.backend.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ToyDTO {

    private Long toyId;
    private String name;
    private String description;
    private Double price;
    private int ageRange;
    private String imageUrl;
    private Instant createdAt;



}
