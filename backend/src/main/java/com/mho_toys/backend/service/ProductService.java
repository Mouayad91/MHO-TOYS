package com.mho_toys.backend.service;

import com.mho_toys.backend.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<ProductDTO> getAllToys();
    ProductDTO addProduct(ProductDTO product);
}
