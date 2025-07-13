package com.mho_toys.backend.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mho_toys.backend.dto.ProductDTO;
import com.mho_toys.backend.exceptions.ApiResponse;
import com.mho_toys.backend.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:5173") 
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

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        ApiResponse response = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Product updated successfully.",
                "/api/v1/admin/products/" + productId
        );

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId, HttpServletRequest request) {
        productService.deleteProduct(productId);
        ApiResponse response = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Product deleted successfully.",
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ApiResponse> updateProductImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request) throws IOException {

        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        ApiResponse response = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Product image updated successfully.",
                request.getRequestURI()

        );
        return ResponseEntity.ok(response);
    }

}
