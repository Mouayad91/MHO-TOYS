package com.mho_toys.backend.security.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * LoginResponse represents the authentication response containing JWT token
 * and user information for the MHO TOYS e-commerce application.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private String jwtToken;
    private String tokenType = "Bearer";
    private String username;
    private String email;
    private Long userId;
    private List<String> roles;
    private Instant expiresAt;
    private boolean twoFactorRequired = false;

    // Constructor for basic login (without 2FA)
    public LoginResponse(String username, String email, Long userId, List<String> roles, String jwtToken, Instant expiresAt) {
        this.username = username;
        this.email = email;
        this.userId = userId;
        this.roles = roles;
        this.jwtToken = jwtToken;
        this.expiresAt = expiresAt;
        this.tokenType = "Bearer";
        this.twoFactorRequired = false;
    }

    // Constructor for 2FA required response
    public LoginResponse(String username, String email, Long userId, List<String> roles, boolean twoFactorRequired) {
        this.username = username;
        this.email = email;
        this.userId = userId;
        this.roles = roles;
        this.twoFactorRequired = twoFactorRequired;
        this.tokenType = "Bearer";
    }

    // Legacy constructor for backward compatibility
    public LoginResponse(String username, List<String> roles, String jwtToken) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
        this.tokenType = "Bearer";
        this.twoFactorRequired = false;
    }
} 