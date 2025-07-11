package com.mho_toys.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long productId;
    private String name;
    private String description;
    private Double price;
    private String ageRange;
    private String imageUrl;




}
