package com.mho_toys.backend.service.ServiceImpl;

import com.mho_toys.backend.dto.ProductDTO;
import com.mho_toys.backend.model.Product;
import com.mho_toys.backend.repository.ProductRepository;
import com.mho_toys.backend.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ProductDTO> getAllToys() {

        List<Product> products = productRepository.findAll();

        return products.stream().map(product-> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

    }

    @Override
    public ProductDTO addProduct(ProductDTO product) {
        Product productFromDB = modelMapper.map(product, Product.class);

        if(productRepository.existsByName(productFromDB.getName())) {
            throw new RuntimeException("Product with the same name already exists");
        }

        Product savedProduct = productRepository.save(productFromDB);
        return modelMapper.map(savedProduct, ProductDTO.class);


    }
}
