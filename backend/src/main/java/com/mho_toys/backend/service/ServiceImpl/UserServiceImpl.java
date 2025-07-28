package com.mho_toys.backend.service.ServiceImpl;

import com.mho_toys.backend.dto.UserDTO;
import com.mho_toys.backend.exceptions.ResourceNotFoundException;
import com.mho_toys.backend.model.ApplicationRole;
import com.mho_toys.backend.model.Role;
import com.mho_toys.backend.model.User;
import com.mho_toys.backend.repository.RoleRepository;
import com.mho_toys.backend.repository.UserRepository;
import com.mho_toys.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final int MAX_FAILED_ATTEMPTS = 5;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        logger.debug("Retrieving all users");
        return userRepository.findAll();
    }

    @Override
    public UserDTO getUserById(Long id) {
        logger.debug("Retrieving user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return convertToDto(user);
    }

    @Override
    public User findByUsername(String username) {
        logger.debug("Finding user by username: {}", username);
        Optional<User> user = userRepository.findByUserName(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    public User findByEmail(String email) {
        logger.debug("Finding user by email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        logger.debug("Finding user by username or email: {}", usernameOrEmail);
        Optional<User> user = userRepository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
        logger.debug("Updating role for user ID: {} to role: {}", userId, roleName);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        ApplicationRole appRole = ApplicationRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        
        user.setRole(role);
        user.setUpdatedBy("SYSTEM"); 
        userRepository.save(user);
        
        logger.info("Role updated for user {} to {}", user.getUserName(), roleName);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        logger.debug("Updating user with ID: {}", userId);
        
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        if (userDTO.getUserName() != null && !userDTO.getUserName().equals(existingUser.getUserName())) {
            if (userRepository.existsByUserName(userDTO.getUserName())) {
                throw new RuntimeException("Username already exists: " + userDTO.getUserName());
            }
            existingUser.setUserName(userDTO.getUserName());
        }
        
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("Email already exists: " + userDTO.getEmail());
            }
            existingUser.setEmail(userDTO.getEmail());
        }
        
        existingUser.setUpdatedBy("SYSTEM");
        User updatedUser = userRepository.save(existingUser);
        
        logger.info("User updated: {}", updatedUser.getUserName());
        return convertToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        logger.debug("Deleting user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        userRepository.delete(user);
        logger.info("User deleted: {}", user.getUserName());
    }

    @Override
    public void enableUser(Long userId) {
        logger.debug("Enabling user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setEnabled(true);
        userRepository.save(user);
        
        logger.info("User enabled: {}", user.getUserName());
    }

    @Override
    public void disableUser(Long userId) {
        logger.debug("Disabling user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setEnabled(false);
        userRepository.save(user);
        
        logger.info("User disabled: {}", user.getUserName());
    }

    @Override
    public void lockAccount(Long userId) {
        logger.debug("Locking account for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.lockAccount();
        userRepository.save(user);
        
        logger.warn("Account locked for user: {}", user.getUserName());
    }

    @Override
    public void unlockAccount(Long userId) {
        logger.debug("Unlocking account for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.unlockAccount();
        userRepository.save(user);
        
        logger.info("Account unlocked for user: {}", user.getUserName());
    }

    @Override
    public void incrementFailedLoginAttempts(String username) {
        logger.debug("Incrementing failed login attempts for user: {}", username);
        
        Optional<User> userOpt = userRepository.findByUserName(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.incrementFailedLoginAttempts();
            
            if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                user.lockAccount();
                logger.warn("Account locked due to {} failed login attempts for user: {}", 
                           MAX_FAILED_ATTEMPTS, username);
            }
            
            userRepository.save(user);
        }
    }

    @Override
    public void resetFailedLoginAttempts(String username) {
        logger.debug("Resetting failed login attempts for user: {}", username);
        
        Optional<User> userOpt = userRepository.findByUserName(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.resetFailedLoginAttempts();
            userRepository.save(user);
        }
    }

    @Override
    public void updateLastLogin(String username) {
        logger.debug("Updating last login for user: {}", username);
        
        Optional<User> userOpt = userRepository.findByUserName(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.updateLastLogin();
            userRepository.save(user);
        }
    }

    @Override
    public boolean isAccountLocked(String username) {
        Optional<User> userOpt = userRepository.findByUserName(username);
        return userOpt.map(user -> !user.isAccountNonLocked()).orElse(false);
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        logger.debug("Changing password for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(1)); // Reset password expiry
        userRepository.save(user);
        
        logger.info("Password changed for user: {}", user.getUserName());
    }

    @Override
    public void resetPassword(String email) {
        logger.debug("Initiating password reset for email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        // In a real implementation, you would:
        // 1. Generate a secure reset token
        // 2. Store it with expiration
        // 3. Send email with reset link
        // For now, we'll just log it
        
        logger.info("Password reset initiated for user: {}", user.getUserName());
        // TODO: Implement actual password reset email functionality
    }

    @Override
    public void generatePasswordResetToken(String email) {
        logger.debug("Generating password reset token for email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        // In a real implementation, you would:
        // 1. Generate a secure reset token
        // 2. Store it with expiration
        // 3. Send email with reset link
        // For now, we'll just log it
        
        logger.info("Password reset token generated for user: {}", user.getUserName());
        // TODO: Implement actual password reset email functionality
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        logger.debug("Resetting password with token");
        
        // In a real implementation, you would:
        // 1. Validate the token
        // 2. Find the user associated with the token
        // 3. Update the password
        // 4. Invalidate the token
        
        // For now, we'll just log it
        logger.info("Password reset completed with token");
        // TODO: Implement actual password reset functionality
    }

    @Override
    public void enableTwoFactorAuth(Long userId, String secret) {
        logger.debug("Enabling 2FA for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setTwoFactorSecret(secret);
        user.setTwoFactorEnabled(true);
        userRepository.save(user);
        
        logger.info("2FA enabled for user: {}", user.getUserName());
    }

    @Override
    public void disableTwoFactorAuth(Long userId) {
        logger.debug("Disabling 2FA for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setTwoFactorSecret(null);
        user.setTwoFactorEnabled(false);
        userRepository.save(user);
        
        logger.info("2FA disabled for user: {}", user.getUserName());
    }

    @Override
    public long countTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long countActiveUsers() {
        return userRepository.countActiveUsersSince(Instant.now().minusSeconds(30 * 24 * 60 * 60)); // 30 days
    }

    @Override
    public long countLockedAccounts() {
        return userRepository.countLockedAccounts();
    }

    @Override
    public long countNewUsersRegisteredSince(Instant since) {
        return userRepository.countNewUsersRegisteredSince(since);
    }

    @Override
    public List<User> findInactiveUsers(Instant cutoffDate) {
        return userRepository.findInactiveUsers(cutoffDate);
    }

    @Override
    public List<User> findUsersWithFailedAttempts(int maxAttempts) {
        return userRepository.findUsersWithFailedAttempts(maxAttempts);
    }

    @Override
    @Transactional
    public void unlockAllAccounts() {
        logger.info("Unlocking all accounts - administrative action");
        userRepository.unlockAllAccounts();
    }

    @Override
    @Transactional
    public void resetAllFailedLoginAttempts() {
        logger.info("Resetting all failed login attempts - administrative action");
        userRepository.resetAllFailedLoginAttempts();
    }

    @Override
    public UserDTO convertToDto(User user) {
        if (user == null) return null;
        
        return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.getTwoFactorSecret(),
                user.isTwoFactorEnabled(),
                user.getSignUpMethod(),
                user.getRole(),
                user.getCreatedDate(),
                user.getUpdatedDate(),
                user.getFailedLoginAttempts(),
                user.getLastLoginDate(),
                user.getAccountLockedDate()
        );
    }

    @Override
    public User convertToEntity(UserDTO userDTO) {
        if (userDTO == null) return null;
        
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setAccountNonLocked(userDTO.isAccountNonLocked());
        user.setAccountNonExpired(userDTO.isAccountNonExpired());
        user.setCredentialsNonExpired(userDTO.isCredentialsNonExpired());
        user.setEnabled(userDTO.isEnabled());
        user.setCredentialsExpiryDate(userDTO.getCredentialsExpiryDate());
        user.setAccountExpiryDate(userDTO.getAccountExpiryDate());
        user.setTwoFactorSecret(userDTO.getTwoFactorSecret());
        user.setTwoFactorEnabled(userDTO.isTwoFactorEnabled());
        user.setSignUpMethod(userDTO.getSignUpMethod());
        user.setRole(userDTO.getRole());
        user.setCreatedDate(userDTO.getCreatedDate());
        user.setUpdatedDate(userDTO.getUpdatedDate());
        user.setFailedLoginAttempts(userDTO.getFailedLoginAttempts());
        user.setLastLoginDate(userDTO.getLastLoginDate());
        user.setAccountLockedDate(userDTO.getAccountLockedDate());
        
        return user;
    }
} 