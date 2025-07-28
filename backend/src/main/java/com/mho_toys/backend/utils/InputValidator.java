package com.mho_toys.backend.utils;

import java.util.regex.Pattern;

/**
 * Input validation utility for backend security
 */
public class InputValidator {
    
    // Common SQL injection patterns
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        ".*(';|--|union|select|insert|drop|delete|update|create|alter|exec|execute).*", 
        Pattern.CASE_INSENSITIVE
    );
    
    // XSS patterns
    private static final Pattern XSS_PATTERN = Pattern.compile(
        ".*(<script|javascript:|vbscript:|onload|onerror|onclick).*", 
        Pattern.CASE_INSENSITIVE
    );
    
    // Valid filename pattern
    private static final Pattern VALID_FILENAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._-]+\\.(jpg|jpeg|png|gif|webp)$", 
        Pattern.CASE_INSENSITIVE
    );
    
    // Valid email pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    /**
     * Sanitize text input by removing dangerous characters
     */
    public static String sanitizeText(String input) {
        if (input == null) {
            return null;
        }
        
        return input
            .replaceAll("[<>\"'&]", "")
            .replaceAll("[\r\n\t]", " ")
            .trim();
    }
    
    /**
     * Validate text input for SQL injection attempts
     */
    public static boolean containsSQLInjection(String input) {
        if (input == null) {
            return false;
        }
        return SQL_INJECTION_PATTERN.matcher(input).matches();
    }
    
    /**
     * Validate text input for XSS attempts
     */
    public static boolean containsXSS(String input) {
        if (input == null) {
            return false;
        }
        return XSS_PATTERN.matcher(input).matches();
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches() && email.length() <= 50;
    }
    
    /**
     * Validate filename for security
     */
    public static boolean isValidFilename(String filename) {
        if (filename == null) {
            return false;
        }
        return VALID_FILENAME_PATTERN.matcher(filename).matches() && filename.length() <= 255;
    }
    
    /**
     * Validate product name
     */
    public static boolean isValidProductName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = name.trim();
        return trimmed.length() >= 3 && 
               trimmed.length() <= 100 && 
               !containsSQLInjection(trimmed) && 
               !containsXSS(trimmed);
    }
    
    /**
     * Validate product description
     */
    public static boolean isValidProductDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = description.trim();
        return trimmed.length() >= 5 && 
               trimmed.length() <= 1000 && 
               !containsSQLInjection(trimmed) && 
               !containsXSS(trimmed);
    }
    
    /**
     * Validate price
     */
    public static boolean isValidPrice(Double price) {
        return price != null && price >= 0 && price <= 999999.99;
    }
    
    /**
     * Validate age range
     */
    public static boolean isValidAgeRange(String ageRange) {
        if (ageRange == null) {
            return false;
        }
        
        Pattern agePattern = Pattern.compile(
            "^(\\d+(-\\d+)?\\s+(Months?|Years?)|\\d+\\+\\s+(Months?|Years?))$"
        );
        
        return agePattern.matcher(ageRange).matches() && ageRange.length() <= 50;
    }
    
    /**
     * Validate username
     */
    public static boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }
        
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
        return usernamePattern.matcher(username).matches() && 
               !containsSQLInjection(username) && 
               !containsXSS(username);
    }
    
    /**
     * Validate password strength
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        
        // At least 8 characters, one uppercase, one lowercase, one digit
        Pattern passwordPattern = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,120}$"
        );
        
        return passwordPattern.matcher(password).matches();
    }
    
    /**
     * Sanitize and validate input text for general use
     */
    public static String validateAndSanitize(String input, int maxLength) {
        if (input == null) {
            return null;
        }
        
        if (containsSQLInjection(input) || containsXSS(input)) {
            throw new IllegalArgumentException("Input contains invalid characters");
        }
        
        String sanitized = sanitizeText(input);
        if (sanitized.length() > maxLength) {
            sanitized = sanitized.substring(0, maxLength);
        }
        
        return sanitized;
    }
    
    /**
     * Validate URL for images
     */
    public static boolean isValidImageUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return true; // Allow empty URLs
        }
        
        try {
            java.net.URL validUrl = new java.net.URL(url);
            String protocol = validUrl.getProtocol();
            return "http".equals(protocol) || "https".equals(protocol);
        } catch (Exception e) {
            return false;
        }
    }
} 