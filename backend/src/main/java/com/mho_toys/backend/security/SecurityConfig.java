package com.mho_toys.backend.security;

import com.mho_toys.backend.model.ApplicationRole;
import com.mho_toys.backend.model.Role;
import com.mho_toys.backend.model.User;
import com.mho_toys.backend.repository.RoleRepository;
import com.mho_toys.backend.repository.UserRepository;
import com.mho_toys.backend.security.jwt.AuthEntryPointJwt;
import com.mho_toys.backend.security.jwt.AuthTokenFilter;
import com.mho_toys.backend.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

import java.time.LocalDate;

/**
 * SecurityConfig provides comprehensive security configuration for the MHO TOYS e-commerce application
 * including JWT authentication, CSRF protection, and security headers.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Security Filter Chain configuration with comprehensive security settings
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS configuration
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowCredentials(true);
                    corsConfig.addAllowedOriginPattern("http://localhost:*");
                    corsConfig.addAllowedOriginPattern("https://localhost:*");
                    corsConfig.addAllowedHeader("*");
                    corsConfig.addAllowedMethod("*");
                    corsConfig.setMaxAge(3600L);
                    return corsConfig;
                }))

                // CSRF Configuration
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                "/api/auth/public/**",
                                "/api/csrf-token",
                                 "/api/auth/set-token",
                                "/images/**",
                                "/error",
                                "/actuator/**"
                        )
                )

                // Session Management - Stateless for JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Authorization Configuration
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints
                        .requestMatchers("/api/auth/public/**").permitAll()
                        .requestMatchers("/api/csrf-token").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        
                        // Static resources
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        
                        // Product endpoints (customers can view, admin can manage)
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers("/api/admin/products/**").hasRole("ADMIN")
                        
                        // Admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Exception Handling
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                )

                // Authentication Provider
                .authenticationProvider(authenticationProvider())
                
                // JWT Token Filter
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)

                // Security Headers
                .headers(headers -> headers
                        .frameOptions().deny()
                        .contentTypeOptions().and()
                        .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                                .maxAgeInSeconds(31536000)
                                .includeSubDomains(true)
                                .preload(true)
                        )
                        .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                )

                // Disable basic auth and form login
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * UserDetailsService Bean
     */
    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    /**
     * DaoAuthenticationProvider Bean
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Authentication Manager Bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Password Encoder Bean - BCrypt with strength 12 for enhanced security
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Initialize default users and roles on application startup
     */
    @Bean
    public CommandLineRunner initializeDefaultData(RoleRepository roleRepository, 
                                                  UserRepository userRepository,
                                                  PasswordEncoder passwordEncoder) {
        return args -> {
            // Initialize roles
            Role userRole = roleRepository.findByRoleName(ApplicationRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role role = new Role(ApplicationRole.ROLE_USER);
                        return roleRepository.save(role);
                    });

            Role adminRole = roleRepository.findByRoleName(ApplicationRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role role = new Role(ApplicationRole.ROLE_ADMIN);
                        return roleRepository.save(role);
                    });

            // Create default admin user if not exists
            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@mhotoys.com",
                        passwordEncoder.encode("1253225"));
                
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
                System.out.println("Default admin user created: admin / 1253225");
            }

            // Create default customer user if not exists
            if (!userRepository.existsByUserName("customer")) {
                User customer = new User("customer", "customer@mhotoys.com",
                        passwordEncoder.encode("Customer@2024!"));
                
                // Set customer properties
                customer.setRole(userRole);
                customer.setAccountNonLocked(true);
                customer.setAccountNonExpired(true);
                customer.setCredentialsNonExpired(true);
                customer.setEnabled(true);
                customer.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                customer.setAccountExpiryDate(LocalDate.now().plusYears(5));
                customer.setTwoFactorEnabled(false);
                customer.setSignUpMethod("system");
                customer.setCreatedBy("SYSTEM");
                
                userRepository.save(customer);
                System.out.println("Default customer user created: customer / Customer@2024!");
            }

            System.out.println("=".repeat(60));
            System.out.println("MHO TOYS SECURITY CONFIGURATION INITIALIZED");
            System.out.println("JWT Authentication: ENABLED");
            System.out.println("CSRF Protection: ENABLED (except public endpoints)");
            System.out.println("Security Headers: ENABLED");
            System.out.println("Session Management: STATELESS");
            System.out.println("=".repeat(60));
        };
    }
}
