package com.mho_toys.backend.security.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SignupRequest represents the data structure for user registration requests
 * in the MHO TOYS e-commerce application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String password;

    // Optional role assignment (defaults to ROLE_USER)
    private Set<String> role;

    // Optional fields for enhanced registration
    private String firstName;
    private String lastName;
    private String phoneNumber;
    
    // Terms and conditions acceptance
    private boolean acceptTerms = false;
    
    // Newsletter subscription preference
    private boolean subscribeNewsletter = false;
} 