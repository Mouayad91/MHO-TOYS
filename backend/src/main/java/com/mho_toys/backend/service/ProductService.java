package com.mho_toys.backend.service;

import com.mho_toys.backend.dto.ProductDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {

    List<ProductDTO> getAllToys();
    ProductDTO addProduct(ProductDTO product);
    ProductDTO updateProduct(Long productId,ProductDTO productDTO);
    ProductDTO deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

}
