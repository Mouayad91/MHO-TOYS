package com.mho_toys.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mho_toys.backend.model.ApplicationRole;
import com.mho_toys.backend.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Optional<Role> findByRoleName(ApplicationRole roleName);
    
    Boolean existsByRoleName(ApplicationRole roleName);
} 