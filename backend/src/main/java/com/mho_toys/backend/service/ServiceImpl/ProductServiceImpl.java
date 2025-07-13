package com.mho_toys.backend.service.ServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.mho_toys.backend.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mho_toys.backend.dto.ProductDTO;
import com.mho_toys.backend.exceptions.ResourceNotFoundException;
import com.mho_toys.backend.model.Product;
import com.mho_toys.backend.repository.ProductRepository;
import com.mho_toys.backend.service.ProductService;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;
    @Value("${project.image}")
    private String path;
    @Value("${image.base.url}")
    private String imageBaseUrl;

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

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (productDTO.getName() != null && !productDTO.getName().equals(existingProduct.getName())) {
            if (productRepository.existsByName(productDTO.getName())) {
                throw new RuntimeException("Product with the same name already exists");
            }
        }

        if (productDTO.getName() != null) {
            existingProduct.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() != null) {
            existingProduct.setPrice(productDTO.getPrice());
        }
        if (productDTO.getAgeRange() != null) {
            existingProduct.setAgeRange(productDTO.getAgeRange());
        }
        if (productDTO.getImageUrl() != null) {
            existingProduct.setImageUrl(productDTO.getImageUrl());
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {


            Product dbProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

            String fileName = fileService.uploadImage(path, image);
            dbProduct.setImageUrl(imageBaseUrl + "/" + fileName);
            Product updatedProduct = productRepository.save(dbProduct);


            return modelMapper.map(dbProduct, ProductDTO.class);
        }
    }

