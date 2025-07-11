package com.mho_toys.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mho_toys.backend.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    boolean existsByName(String name);
}
