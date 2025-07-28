package com.mho_toys.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mho_toys.backend.model.ApplicationRole;
import com.mho_toys.backend.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    /**
     * Find a role by its name
     * @param roleName the ApplicationRole enum value
     * @return Optional<Role> containing the role if found
     */
    Optional<Role> findByRoleName(ApplicationRole roleName);
    
    /**
     * Check if a role exists by its name
     * @param roleName the ApplicationRole enum value
     * @return true if the role exists, false otherwise
     */
    Boolean existsByRoleName(ApplicationRole roleName);
} 