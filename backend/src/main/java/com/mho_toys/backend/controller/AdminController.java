package com.mho_toys.backend.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mho_toys.backend.dto.UserDTO;
import com.mho_toys.backend.model.User;
import com.mho_toys.backend.security.response.MessageResponse;
import com.mho_toys.backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "${frontend.url}", maxAge = 3600, allowCredentials = "true")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    /**
     * Get all users (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@AuthenticationPrincipal UserDetails adminUser) {
        logger.info("Admin {} requested all users list", adminUser.getUsername());
        
        List<User> users = userService.getAllUsers();
        
        logger.debug("Returning {} users to admin {}", users.size(), adminUser.getUsername());
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by ID (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails adminUser) {
        logger.info("Admin {} requested user details for ID: {}", adminUser.getUsername(), id);
        
        try {
            UserDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error retrieving user {} by admin {}: {}", id, adminUser.getUsername(), e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update user role (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<MessageResponse> updateUserRole(@PathVariable Long userId,
                                                         @RequestParam String roleName,
                                                         @AuthenticationPrincipal UserDetails adminUser,
                                                         HttpServletRequest request) {
        logger.info("Admin {} updating role for user {} to {} from IP: {}", 
                   adminUser.getUsername(), userId, roleName, getClientIpAddress(request));
        
        try {
            userService.updateUserRole(userId, roleName);
            
            logger.info("Role updated successfully for user {} to {} by admin {}", 
                       userId, roleName, adminUser.getUsername());
            
            return ResponseEntity.ok(MessageResponse.success("User role updated successfully"));
        } catch (Exception e) {
            logger.error("Role update failed for user {} by admin {}: {}", 
                        userId, adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Role update failed: " + e.getMessage()));
        }
    }

    /**
     * Enable user account (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/users/{userId}/enable")
    public ResponseEntity<MessageResponse> enableUser(@PathVariable Long userId,
                                                     @AuthenticationPrincipal UserDetails adminUser,
                                                     HttpServletRequest request) {
        logger.info("Admin {} enabling user {} from IP: {}", 
                   adminUser.getUsername(), userId, getClientIpAddress(request));
        
        try {
            userService.enableUser(userId);
            
            logger.info("User {} enabled successfully by admin {}", userId, adminUser.getUsername());
            return ResponseEntity.ok(MessageResponse.success("User enabled successfully"));
        } catch (Exception e) {
            logger.error("Enable user {} failed by admin {}: {}", 
                        userId, adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Enable user failed: " + e.getMessage()));
        }
    }

    /**
     * Disable user account (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/users/{userId}/disable")
    public ResponseEntity<MessageResponse> disableUser(@PathVariable Long userId,
                                                      @AuthenticationPrincipal UserDetails adminUser,
                                                      HttpServletRequest request) {
        logger.warn("Admin {} disabling user {} from IP: {}", 
                   adminUser.getUsername(), userId, getClientIpAddress(request));
        
        try {
            userService.disableUser(userId);
            
            logger.warn("User {} disabled by admin {}", userId, adminUser.getUsername());
            return ResponseEntity.ok(MessageResponse.success("User disabled successfully"));
        } catch (Exception e) {
            logger.error("Disable user {} failed by admin {}: {}", 
                        userId, adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Disable user failed: " + e.getMessage()));
        }
    }

    /**
     * Lock user account (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/users/{userId}/lock")
    public ResponseEntity<MessageResponse> lockUser(@PathVariable Long userId,
                                                   @AuthenticationPrincipal UserDetails adminUser,
                                                   HttpServletRequest request) {
        logger.warn("Admin {} locking user {} from IP: {}", 
                   adminUser.getUsername(), userId, getClientIpAddress(request));
        
        try {
            userService.lockAccount(userId);
            
            logger.warn("User {} locked by admin {}", userId, adminUser.getUsername());
            return ResponseEntity.ok(MessageResponse.success("User account locked successfully"));
        } catch (Exception e) {
            logger.error("Lock user {} failed by admin {}: {}", 
                        userId, adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Lock user failed: " + e.getMessage()));
        }
    }

    /**
     * Unlock user account (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/users/{userId}/unlock")
    public ResponseEntity<MessageResponse> unlockUser(@PathVariable Long userId,
                                                     @AuthenticationPrincipal UserDetails adminUser,
                                                     HttpServletRequest request) {
        logger.info("Admin {} unlocking user {} from IP: {}", 
                   adminUser.getUsername(), userId, getClientIpAddress(request));
        
        try {
            userService.unlockAccount(userId);
            
            logger.info("User {} unlocked by admin {}", userId, adminUser.getUsername());
            return ResponseEntity.ok(MessageResponse.success("User account unlocked successfully"));
        } catch (Exception e) {
            logger.error("Unlock user {} failed by admin {}: {}", 
                        userId, adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Unlock user failed: " + e.getMessage()));
        }
    }

    /**
     * Delete user account (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long userId,
                                                     @AuthenticationPrincipal UserDetails adminUser,
                                                     HttpServletRequest request) {
        logger.warn("Admin {} attempting to delete user {} from IP: {}", 
                   adminUser.getUsername(), userId, getClientIpAddress(request));
        
        try {
            // Prevent admin from deleting themselves
            User currentAdmin = userService.findByUsername(adminUser.getUsername());
            if (currentAdmin.getUserId().equals(userId)) {
                logger.warn("Admin {} attempted to delete their own account", adminUser.getUsername());
                return ResponseEntity.badRequest()
                        .body(MessageResponse.error("Cannot delete your own account"));
            }
            
            userService.deleteUser(userId);
            
            logger.warn("User {} deleted by admin {}", userId, adminUser.getUsername());
            return ResponseEntity.ok(MessageResponse.success("User deleted successfully"));
        } catch (Exception e) {
            logger.error("Delete user {} failed by admin {}: {}", 
                        userId, adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Delete user failed: " + e.getMessage()));
        }
    }

    /**
     * Get admin dashboard statistics
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getAdminStatistics(@AuthenticationPrincipal UserDetails adminUser) {
        logger.info("Admin {} requested dashboard statistics", adminUser.getUsername());
        
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // Get user statistics
            long totalUsers = userService.countTotalUsers();
            long activeUsers = userService.countActiveUsers();
            long lockedAccounts = userService.countLockedAccounts();
            
            // Recent registrations
            Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
            Instant oneMonthAgo = Instant.now().minus(30, ChronoUnit.DAYS);
            long newUsersLastWeek = userService.countNewUsersRegisteredSince(oneWeekAgo);
            long newUsersLastMonth = userService.countNewUsersRegisteredSince(oneMonthAgo);
            
            // Security statistics
            long usersWithFailedAttempts = userService.findUsersWithFailedAttempts(1).size();
            
            statistics.put("totalUsers", totalUsers);
            statistics.put("activeUsers", activeUsers);
            statistics.put("lockedAccounts", lockedAccounts);
            statistics.put("newUsersLastWeek", newUsersLastWeek);
            statistics.put("newUsersLastMonth", newUsersLastMonth);
            statistics.put("usersWithFailedAttempts", usersWithFailedAttempts);
            
            logger.debug("Returning statistics to admin {}: {}", adminUser.getUsername(), statistics);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Error retrieving statistics for admin {}: {}", adminUser.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get users with failed login attempts (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/security/failed-attempts")
    public ResponseEntity<List<User>> getUsersWithFailedAttempts(@RequestParam(defaultValue = "3") int minAttempts,
                                                                @AuthenticationPrincipal UserDetails adminUser) {
        logger.info("Admin {} requested users with {} or more failed attempts", adminUser.getUsername(), minAttempts);
        
        List<User> users = userService.findUsersWithFailedAttempts(minAttempts);
        return ResponseEntity.ok(users);
    }

    /**
     * Get inactive users (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/security/inactive-users")
    public ResponseEntity<List<User>> getInactiveUsers(@RequestParam(defaultValue = "90") int daysSinceLastLogin,
                                                      @AuthenticationPrincipal UserDetails adminUser) {
        logger.info("Admin {} requested users inactive for {} days", adminUser.getUsername(), daysSinceLastLogin);
        
        Instant cutoffDate = Instant.now().minus(daysSinceLastLogin, ChronoUnit.DAYS);
        List<User> users = userService.findInactiveUsers(cutoffDate);
        return ResponseEntity.ok(users);
    }

    /**
     * Reset all failed login attempts (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/security/reset-failed-attempts")
    public ResponseEntity<MessageResponse> resetAllFailedAttempts(@AuthenticationPrincipal UserDetails adminUser,
                                                                 HttpServletRequest request) {
        logger.warn("Admin {} resetting all failed login attempts from IP: {}", 
                   adminUser.getUsername(), getClientIpAddress(request));
        
        try {
            userService.resetAllFailedLoginAttempts();
            
            logger.info("All failed login attempts reset by admin {}", adminUser.getUsername());
            return ResponseEntity.ok(MessageResponse.success("All failed login attempts reset successfully"));
        } catch (Exception e) {
            logger.error("Reset failed attempts failed by admin {}: {}", 
                        adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to reset login attempts: " + e.getMessage()));
        }
    }

    /**
     * Unlock all user accounts (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/security/unlock-all-accounts")
    public ResponseEntity<MessageResponse> unlockAllAccounts(@AuthenticationPrincipal UserDetails adminUser,
                                                            HttpServletRequest request) {
        logger.warn("Admin {} unlocking all accounts from IP: {}", 
                   adminUser.getUsername(), getClientIpAddress(request));
        
        try {
            userService.unlockAllAccounts();
            
            logger.info("All accounts unlocked by admin {}", adminUser.getUsername());
            return ResponseEntity.ok(MessageResponse.success("All accounts unlocked successfully"));
        } catch (Exception e) {
            logger.error("Unlock all accounts failed by admin {}: {}", 
                        adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Failed to unlock accounts: " + e.getMessage()));
        }
    }

    /**
     * Update user profile (admin only)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId,
                                             @RequestBody UserDTO userDTO,
                                             @AuthenticationPrincipal UserDetails adminUser) {
        logger.info("Admin {} updating user {}", adminUser.getUsername(), userId);
        
        try {
            UserDTO updatedUser = userService.updateUser(userId, userDTO);
            
            logger.info("User {} updated by admin {}", userId, adminUser.getUsername());
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            logger.error("Update user {} failed by admin {}: {}", 
                        userId, adminUser.getUsername(), e.getMessage());
            
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get client IP address from request
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
            return request.getRemoteAddr();
        } else {
            return xForwardedForHeader.split(",")[0].trim();
        }
    }
} 