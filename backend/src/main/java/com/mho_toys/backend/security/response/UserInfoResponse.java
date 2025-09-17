package com.mho_toys.backend.security.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    
    private Long id;
    private String username;
    private String email;
    
    // Account status information
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean enabled;
    
    // Account expiration dates
    private LocalDate credentialsExpiryDate;
    private LocalDate accountExpiryDate;
    
    // Security features
    private boolean isTwoFactorEnabled;
    private List<String> roles;
    
    // Additional profile information
    private String signUpMethod;
    private Instant lastLoginDate;
    private Instant createdDate;
    private int failedLoginAttempts;

    // Constructor for basic user info (commonly used)
    public UserInfoResponse(Long id, String username, String email, 
                           boolean accountNonLocked, boolean accountNonExpired,
                           boolean credentialsNonExpired, boolean enabled,
                           LocalDate credentialsExpiryDate, LocalDate accountExpiryDate,
                           boolean isTwoFactorEnabled, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.accountNonLocked = accountNonLocked;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.credentialsExpiryDate = credentialsExpiryDate;
        this.accountExpiryDate = accountExpiryDate;
        this.isTwoFactorEnabled = isTwoFactorEnabled;
        this.roles = roles;
    }

    // Constructor for minimal user info (public profile)
    public UserInfoResponse(Long id, String username, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.accountNonLocked = true;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.isTwoFactorEnabled = false;
    }
} 