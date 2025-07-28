package com.mho_toys.backend.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mho_toys.backend.security.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * AuthTokenFilter processes JWT tokens from incoming requests and sets up the security context
 * for authenticated users in the MHO TOYS e-commerce application.
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                  @NonNull HttpServletResponse response, 
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        
        try {
            String jwt = parseJwt(request);
            
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                
                logger.debug("JWT token valid for user: {}", username);
                
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Additional validation: check if token is valid for this specific user
                if (jwtUtils.validateTokenForUser(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(
                                userDetails, 
                                null, 
                                userDetails.getAuthorities()
                            );
                    
                    logger.debug("Roles from JWT: {}", userDetails.getAuthorities());
                    
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.warn("JWT token validation failed for user: {}", username);
                }
            } else {
                logger.debug("No valid JWT token found in request");
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
            // Clear security context on any authentication error
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from the Authorization header or cookies
     */
    private String parseJwt(HttpServletRequest request) {
        // First try Authorization header
        String jwt = jwtUtils.getJwtFromHeader(request);
        
        // If no header token, try cookies
        if (jwt == null) {
            jwt = getJwtFromCookies(request);
        }
        
        logger.debug("JWT token found: {}", jwt != null ? "[PROTECTED]" : "null");
        return jwt;
    }

    /**
     * Extract JWT token from httpOnly cookies
     */
    private String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    logger.debug("JWT token found in cookie");
                    return cookie.getValue();
                }
            }
        }
        logger.debug("No JWT token found in cookies");
        return null;
    }

    /**
     * Skip JWT processing for certain paths (like public authentication endpoints)
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        
        // Skip JWT validation for public authentication endpoints
        boolean shouldSkip = path.startsWith("/api/auth/public/") || 
                           path.startsWith("/api/csrf-token") ||
                           path.equals("/error") ||
                           path.startsWith("/images/");
        
        if (shouldSkip) {
            logger.debug("Skipping JWT filter for path: {}", path);
        }
        
        return shouldSkip;
    }
} 