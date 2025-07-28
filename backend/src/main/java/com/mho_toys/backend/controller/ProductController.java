package com.mho_toys.backend.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.mho_toys.backend.security.response.MessageResponse;
import com.mho_toys.backend.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${frontend.url}", maxAge = 3600, allowCredentials = "true")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllToys() {
        logger.debug("Public request for all products");
        
        try {
            List<ProductDTO> products = productService.getAllToys();
            logger.info("Returned {} products for public view", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Error retrieving products: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        logger.debug("Public request for product ID: {}", productId);
        
        try {
            ProductDTO product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error("Error retrieving product {}: {}", productId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/products")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                       @AuthenticationPrincipal UserDetails adminUser,
                                       HttpServletRequest request) {
        logger.info("Admin {} adding new product: {} from IP: {}", 
                   adminUser.getUsername(), productDTO.getName(), getClientIpAddress(request));
        
        try {
            ProductDTO savedProduct = productService.addProduct(productDTO);
            
            logger.info("Product added successfully: {} by admin {}", 
                       savedProduct.getName(), adminUser.getUsername());
            
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Failed to add product by admin {}: {}", 
                        adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to add product: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error adding product by admin {}: {}", 
                        adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Internal server error"));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, 
                                          @Valid @RequestBody ProductDTO productDTO,
                                          @AuthenticationPrincipal UserDetails adminUser,
                                          HttpServletRequest request) {
        logger.info("Admin {} updating product ID: {} from IP: {}", 
                   adminUser.getUsername(), productId, getClientIpAddress(request));
        
        try {
            ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
            
            logger.info("Product {} updated successfully by admin {}", 
                       productId, adminUser.getUsername());

            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            logger.error("Failed to update product {} by admin {}: {}", 
                        productId, adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to update product: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error updating product {} by admin {}: {}", 
                        productId, adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Internal server error"));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId,
                                                    @AuthenticationPrincipal UserDetails adminUser, 
                                                    HttpServletRequest request) {
        logger.warn("Admin {} deleting product ID: {} from IP: {}", 
                   adminUser.getUsername(), productId, getClientIpAddress(request));
        
        try {
            productService.deleteProduct(productId);
            
            ApiResponse response = new ApiResponse(
                    LocalDateTime.now(),
                    HttpStatus.OK.value(),
                    "Product deleted successfully by " + adminUser.getUsername(),
                    request.getRequestURI()
            );
            
            logger.warn("Product {} deleted by admin {}", productId, adminUser.getUsername());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Failed to delete product {} by admin {}: {}", 
                        productId, adminUser.getUsername(), e.getMessage());
            
            ApiResponse response = new ApiResponse(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to delete product: " + e.getMessage(),
                    request.getRequestURI()
            );
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Unexpected error deleting product {} by admin {}: {}", 
                        productId, adminUser.getUsername(), e.getMessage());
            
            ApiResponse response = new ApiResponse(
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal server error",
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ApiResponse> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image,
                                                         @AuthenticationPrincipal UserDetails adminUser,
                                                         HttpServletRequest request) throws IOException {
        logger.info("Admin {} updating image for product ID: {} from IP: {}", 
                   adminUser.getUsername(), productId, getClientIpAddress(request));
        
        try {
            if (image.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Image file is required",
                        request.getRequestURI()
                ));
            }

            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(new ApiResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Only image files are allowed",
                        request.getRequestURI()
                ));
            }

            if (image.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(new ApiResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Image file size must be less than 10MB",
                        request.getRequestURI()
                ));
            }

            ProductDTO updatedProduct = productService.updateProductImage(productId, image);
            
            ApiResponse response = new ApiResponse(
                    LocalDateTime.now(),
                    HttpStatus.OK.value(),
                    "Product image updated successfully by " + adminUser.getUsername(),
                    request.getRequestURI()
            );
            
            logger.info("Product {} image updated successfully by admin {}", 
                       productId, adminUser.getUsername());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Failed to update image for product {} by admin {}: {}", 
                        productId, adminUser.getUsername(), e.getMessage());
            
            ApiResponse response = new ApiResponse(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to update product image: " + e.getMessage(),
                    request.getRequestURI()
            );
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Unexpected error updating image for product {} by admin {}: {}", 
                        productId, adminUser.getUsername(), e.getMessage());
            
            ApiResponse response = new ApiResponse(
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal server error",
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
            return request.getRemoteAddr();
        } else {
            return xForwardedForHeader.split(",")[0].trim();
        }
    }
}
