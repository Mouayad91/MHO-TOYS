package com.mho_toys.backend.controller;

import com.mho_toys.backend.dto.ProductDTO;
import com.mho_toys.backend.exceptions.ErrorResponse;
import com.mho_toys.backend.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ErrorResponse> deleteProduct(@PathVariable Long productId, HttpServletRequest request) {
        productService.deleteProduct(productId);
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Product deleted successfully.",
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }
}
