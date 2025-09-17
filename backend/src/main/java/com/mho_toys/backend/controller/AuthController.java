package com.mho_toys.backend.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mho_toys.backend.model.ApplicationRole;
import com.mho_toys.backend.model.Role;
import com.mho_toys.backend.model.User;
import com.mho_toys.backend.repository.RoleRepository;
import com.mho_toys.backend.repository.UserRepository;
import com.mho_toys.backend.security.jwt.JwtUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.mho_toys.backend.security.request.LoginRequest;
import com.mho_toys.backend.security.request.SignupRequest;
import com.mho_toys.backend.security.response.LoginResponse;
import com.mho_toys.backend.security.response.MessageResponse;
import com.mho_toys.backend.security.response.UserInfoResponse;
import com.mho_toys.backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${frontend.url}", maxAge = 3600, allowCredentials = "true")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;
    
    @Autowired
    private com.mho_toys.backend.security.service.UserDetailsServiceImpl userDetailsService;

    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                             HttpServletRequest request) {
        logger.info("Login attempt for user: {} from IP: {}", 
                   loginRequest.getUsername(), getClientIpAddress(request));

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), 
                            loginRequest.getPassword()
                    )
            );

            // Update last login and reset failed attempts on successful authentication
            userService.updateLastLogin(loginRequest.getUsername());
            userService.resetFailedLoginAttempts(loginRequest.getUsername());

        } catch (BadCredentialsException e) {
            logger.warn("Bad credentials for user: {} from IP: {}", 
                       loginRequest.getUsername(), getClientIpAddress(request));
            
            // Increment failed login attempts
            userService.incrementFailedLoginAttempts(loginRequest.getUsername());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username or password");
            errorResponse.put("status", false);
            errorResponse.put("timestamp", Instant.now().toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
            
        } catch (LockedException e) {
            logger.warn("Account locked for user: {} from IP: {}", 
                       loginRequest.getUsername(), getClientIpAddress(request));
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Account is locked due to multiple failed login attempts");
            errorResponse.put("status", false);
            errorResponse.put("timestamp", Instant.now().toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.LOCKED);
            
        } catch (DisabledException e) {
            logger.warn("Disabled account access attempt for user: {} from IP: {}", 
                       loginRequest.getUsername(), getClientIpAddress(request));
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Account is disabled");
            errorResponse.put("status", false);
            errorResponse.put("timestamp", Instant.now().toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            
        } catch (AuthenticationException e) {
            logger.error("Authentication error for user: {} from IP: {} - {}", 
                        loginRequest.getUsername(), getClientIpAddress(request), e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Authentication failed");
            errorResponse.put("status", false);
            errorResponse.put("timestamp", Instant.now().toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate JWT token with appropriate expiration
        String jwtToken;
        Instant expiresAt;
        
        if (loginRequest.isRememberMe()) {
            // Extended expiration for "remember me"
            jwtToken = jwtUtils.generateTokenWithCustomExpiration(userDetails, 7 * 24 * 60 * 60 * 1000L); // 7 days
            expiresAt = Instant.now().plusSeconds(7 * 24 * 60 * 60);
        } else {
            jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
            expiresAt = Instant.now().plusSeconds(24 * 60 * 60); // 24 hours default
        }

        // Collect roles from UserDetails
        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        // Get additional user information
        User user = userService.findByUsername(userDetails.getUsername());

        // Check if 2FA is enabled
        if (user.isTwoFactorEnabled()) {
            // Return response indicating 2FA is required
            LoginResponse response = new LoginResponse(
                    user.getUserName(),
                    user.getEmail(),
                    user.getUserId(),
                    roles,
                    true // 2FA required
            );
            return ResponseEntity.ok(response);
        }

        // Prepare the login response
        LoginResponse response = new LoginResponse(
                user.getUserName(),
                user.getEmail(),
                user.getUserId(),
                roles,
                jwtToken,
                expiresAt
        );

        logger.info("Successful login for user: {} from IP: {}", 
                   userDetails.getUsername(), getClientIpAddress(request));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/public/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                         HttpServletRequest request) {
        logger.info("Registration attempt for username: {} email: {} from IP: {}", 
                   signUpRequest.getUsername(), signUpRequest.getEmail(), getClientIpAddress(request));

        // Validate username availability
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            logger.warn("Registration failed - username already exists: {}", signUpRequest.getUsername());
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Username is already taken!"));
        }

        // Validate email availability
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            logger.warn("Registration failed - email already exists: {}", signUpRequest.getEmail());
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Email is already in use!"));
        }

        // Create new user account
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())
        );

        // Set role (default to ROLE_USER)
        Set<String> strRoles = signUpRequest.getRole();
        Role role;

        if (strRoles == null || strRoles.isEmpty()) {
            role = roleRepository.findByRoleName(ApplicationRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Default role not found."));
        } else {
            String roleStr = strRoles.iterator().next();
            if (roleStr.equals("admin") || roleStr.equals("ROLE_ADMIN")) {
                // Admin registration should be restricted in production
                role = roleRepository.findByRoleName(ApplicationRole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Admin role not found."));
                logger.warn("Admin role assignment for user: {} from IP: {}", 
                           signUpRequest.getUsername(), getClientIpAddress(request));
            } else {
                role = roleRepository.findByRoleName(ApplicationRole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: User role not found."));
            }
        }

        // Set security properties
        user.setRole(role);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        user.setAccountExpiryDate(LocalDate.now().plusYears(5));
        user.setTwoFactorEnabled(false);
        user.setSignUpMethod("email");
        user.setCreatedBy("SYSTEM");

        // Save user
        User savedUser = userRepository.save(user);

        logger.info("User registered successfully: {} with role: {} from IP: {}", 
                   savedUser.getUserName(), role.getRoleName(), getClientIpAddress(request));

        return ResponseEntity.ok(MessageResponse.success("User registered successfully!"));
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        logger.debug("Profile request for user: {}", userDetails.getUsername());

        User user = userService.findByUsername(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.isTwoFactorEnabled(),
                roles,
                user.getSignUpMethod(),
                user.getLastLoginDate(),
                user.getCreatedDate(),
                user.getFailedLoginAttempts()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/username")
    public ResponseEntity<Map<String, String>> getCurrentUsername(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, String> response = new HashMap<>();
        response.put("username", userDetails != null ? userDetails.getUsername() : "");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> passwordData) {
        
        String currentPassword = passwordData.get("currentPassword");
        String newPassword = passwordData.get("newPassword");

        if (currentPassword == null || newPassword == null) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Current password and new password are required"));
        }

        try {
            User user = userService.findByUsername(userDetails.getUsername());
            userService.changePassword(user.getUserId(), currentPassword, newPassword);
            
            logger.info("Password changed for user: {}", userDetails.getUsername());
            return ResponseEntity.ok(MessageResponse.success("Password changed successfully"));
        } catch (Exception e) {
            logger.error("Password change failed for user: {} - {}", userDetails.getUsername(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Password change failed: " + e.getMessage()));
        }
    }



    @PostMapping("/public/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        logger.info("Password reset requested for email: {}", email);
        
        try {
            userService.generatePasswordResetToken(email);
            return ResponseEntity.ok(MessageResponse.success("Password reset instructions sent to your email"));
        } catch (Exception e) {
            // Don't reveal if email exists or not for security
            logger.warn("Password reset attempt for non-existent email: {}", email);
            return ResponseEntity.ok(MessageResponse.success("If the email exists, password reset instructions have been sent"));
        }
    }

    @PostMapping("/public/reset-password")
    public ResponseEntity<?> resetPassword(
        @RequestParam String token, 
        @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            logger.info("Password reset completed successfully for token");
            return ResponseEntity.ok(MessageResponse.success("Password reset successfully!"));
        } catch (RuntimeException e) {
            logger.warn("Password reset failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.error("Error resetting password: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> secureLogout(HttpServletRequest request, 
                                                      HttpServletResponse response) {
        try {
            // Clear the JWT cookie
            Cookie jwtCookie = new Cookie("jwt", "");
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(0); // Expire immediately
            
            response.addCookie(jwtCookie);
            
            // Clear security context
            SecurityContextHolder.clearContext();
            
            logger.info("User logged out successfully");
            return ResponseEntity.ok(MessageResponse.success("Logged out successfully"));
        } catch (Exception e) {
            logger.error("Error during logout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Logout failed"));
        }
    }

    @PostMapping("/public/init-admin")
    public ResponseEntity<?> initializeAdminUser() {
        logger.info("Initializing admin user");
        
        try {
            // Check if admin user exists
            if (userRepository.existsByUserName("admin")) {
                logger.info("Admin user already exists");
                return ResponseEntity.ok(MessageResponse.success("Admin user already exists"));
            }
            
            // Create admin user
            User admin = new User("admin", "admin@mhotoys.com", encoder.encode("1253225"));
            
            // Get admin role
            Role adminRole = roleRepository.findByRoleName(ApplicationRole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            
            // Set admin properties
            admin.setRole(adminRole);
            admin.setAccountNonLocked(true);
            admin.setAccountNonExpired(true);
            admin.setCredentialsNonExpired(true);
            admin.setEnabled(true);
            admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
            admin.setAccountExpiryDate(LocalDate.now().plusYears(5));
            admin.setTwoFactorEnabled(false);
            admin.setSignUpMethod("system");
            admin.setCreatedBy("SYSTEM");
            
            userRepository.save(admin);
            
            logger.info("Admin user created successfully");
            return ResponseEntity.ok(MessageResponse.success("Admin user created successfully"));
        } catch (Exception e) {
            logger.error("Error creating admin user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Error creating admin user: " + e.getMessage()));
        }
    }

    @PostMapping("/set-token")
    public ResponseEntity<?> setAuthToken(@RequestBody Map<String, String> request, 
                                         HttpServletResponse response) {
        try {
            String token = request.get("token");
            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Token is required"));
            }

            // Create httpOnly cookie for the JWT token
            Cookie jwtCookie = new Cookie("jwtToken", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false); // Set to true in production with HTTPS
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 24 hours
            // Note: SameSite attribute would need manual ResponseCookie in newer Spring versions
            
            response.addCookie(jwtCookie);
            
            return ResponseEntity.ok(Map.of("success", true, "message", "Token set successfully"));
        } catch (Exception e) {
            logger.error("Error setting auth token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to set token"));
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
            return request.getRemoteAddr();
        } else {
            return xForwardedForHeader.split(",")[0].trim();
        }
    }
} 