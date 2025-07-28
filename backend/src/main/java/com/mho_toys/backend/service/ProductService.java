package com.mho_toys.backend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mho_toys.backend.dto.ProductDTO;

@Service
public interface ProductService {

    List<ProductDTO> getAllToys();
    ProductDTO getProductById(Long productId);
    ProductDTO addProduct(ProductDTO product);
    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
    ProductDTO deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
