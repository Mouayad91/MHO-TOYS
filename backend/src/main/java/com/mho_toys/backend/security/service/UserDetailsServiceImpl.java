package com.mho_toys.backend.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mho_toys.backend.model.User;
import com.mho_toys.backend.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading user details for username: {}", username);
        
        try {
            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> {
                        logger.error("User not found with username: {}", username);
                        return new UsernameNotFoundException("User Not Found with username: " + username);
                    });

            logger.debug("User found: {}, Enabled: {}, Account Non Locked: {}", 
                        username, user.isEnabled(), user.isAccountNonLocked());

            // Check if account is locked due to failed login attempts
            if (user.isAccountLockedDueToFailedAttempts()) {
                logger.warn("Account locked due to failed login attempts for user: {}", username);
                user.lockAccount();
                userRepository.save(user);
            }

            return UserDetailsImpl.build(user);
        } catch (Exception e) {
            logger.error("Error loading user by username {}: {}", username, e.getMessage());
            throw new UsernameNotFoundException("Error loading user: " + e.getMessage());
        }
    }


    @Transactional
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        logger.debug("Loading user details for email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UsernameNotFoundException("User Not Found with email: " + email);
                });

        logger.debug("User found by email: {}, Username: {}", email, user.getUserName());

        // Check if account is locked due to failed login attempts
        if (user.isAccountLockedDueToFailedAttempts()) {
            logger.warn("Account locked due to failed login attempts for user: {}", user.getUserName());
            user.lockAccount();
            userRepository.save(user);
        }

        return UserDetailsImpl.build(user);
    }

    @Transactional
    public UserDetails loadUserByUsernameOrEmail(String usernameOrEmail) throws UsernameNotFoundException {
        logger.debug("Loading user details for username or email: {}", usernameOrEmail);
        
        User user = userRepository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> {
                    logger.error("User not found with username or email: {}", usernameOrEmail);
                    return new UsernameNotFoundException("User Not Found with username or email: " + usernameOrEmail);
                });

        logger.debug("User found: {}, Login method: {}", 
                    user.getUserName(), 
                    usernameOrEmail.equals(user.getUserName()) ? "username" : "email");

        if (user.isAccountLockedDueToFailedAttempts()) {
            logger.warn("Account locked due to failed login attempts for user: {}", user.getUserName());
            user.lockAccount();
            userRepository.save(user);
        }

        return UserDetailsImpl.build(user);
    }
} 