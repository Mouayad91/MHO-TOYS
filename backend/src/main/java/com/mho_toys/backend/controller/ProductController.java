package com.mho_toys.backend.controller;

import com.mho_toys.backend.dto.ProductDTO;
import com.mho_toys.backend.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("/products")
    public List<ProductDTO> getAllToys() {

        List<ProductDTO> product = productService.getAllToys();
        return new ResponseEntity<>(product, HttpStatus.OK).getBody();
    }

    @PostMapping("/admin/products")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO){
        ProductDTO savedProduct = productService.addProduct(productDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }


}
