package com.mho_toys.backend.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mho_toys.backend.dto.UserDTO;
import com.mho_toys.backend.model.User;

/**
 * UserService interface defining user management operations
 * for the MHO TOYS e-commerce application.
 */
@Service
public interface UserService {
    
    List<User> getAllUsers();
    UserDTO getUserById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByUsernameOrEmail(String usernameOrEmail);
    
    void updateUserRole(Long userId, String roleName);
    UserDTO updateUser(Long userId, UserDTO userDTO);
    void deleteUser(Long userId);
    
    void enableUser(Long userId);
    void disableUser(Long userId);
    void lockAccount(Long userId);
    void unlockAccount(Long userId);
    
    void incrementFailedLoginAttempts(String username);
    void resetFailedLoginAttempts(String username);
    void updateLastLogin(String username);
    boolean isAccountLocked(String username);
    
    void changePassword(Long userId, String currentPassword, String newPassword);
    void resetPassword(String email);
    void generatePasswordResetToken(String email);
    void resetPassword(String token, String newPassword);
    
    void enableTwoFactorAuth(Long userId, String secret);
    void disableTwoFactorAuth(Long userId);
    
    long countTotalUsers();
    long countActiveUsers();
    long countLockedAccounts();
    long countNewUsersRegisteredSince(Instant since);
    List<User> findInactiveUsers(Instant cutoffDate);
    List<User> findUsersWithFailedAttempts(int maxAttempts);
    
    // Bulk operations for admin
    void unlockAllAccounts();
    void resetAllFailedLoginAttempts();
    
    UserDTO convertToDto(User user);
    User convertToEntity(UserDTO userDTO);
} 