package com.mho_toys.backend.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.mho_toys.backend.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDTO represents user data for transfer between service layers
 * in the MHO TOYS e-commerce application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private Long userId;
    private String userName;
    private String email;
    
    // Account status
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean enabled;
    
    // Account expiration
    private LocalDate credentialsExpiryDate;
    private LocalDate accountExpiryDate;
    
    // Security
    private String twoFactorSecret;
    private boolean isTwoFactorEnabled;
    private String signUpMethod;
    
    // Role information
    private Role role;
    
    // Audit information
    private Instant createdDate;
    private Instant updatedDate;
    
    // Security tracking
    private int failedLoginAttempts;
    private Instant lastLoginDate;
    private Instant accountLockedDate;

    // Constructor for basic user info
    public UserDTO(Long userId, String userName, String email, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.accountNonLocked = true;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.isTwoFactorEnabled = false;
        this.failedLoginAttempts = 0;
    }

    // Constructor without sensitive information (for public display)
    public UserDTO(Long userId, String userName, String email, boolean enabled, Role role, Instant createdDate) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.enabled = enabled;
        this.role = role;
        this.createdDate = createdDate;
    }
}
