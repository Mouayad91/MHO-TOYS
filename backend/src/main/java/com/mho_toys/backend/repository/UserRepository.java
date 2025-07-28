package com.mho_toys.backend.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mho_toys.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUserNameOrEmail(String userName, String email);

    Boolean existsByUserName(String userName);
    
    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.accountNonLocked = false")
    List<User> findAllLockedAccounts();

    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts >= :maxAttempts")
    List<User> findUsersWithFailedAttempts(@Param("maxAttempts") int maxAttempts);

    @Query("SELECT u FROM User u WHERE u.lastLoginDate < :cutoffDate")
    List<User> findInactiveUsers(@Param("cutoffDate") Instant cutoffDate);

    @Query("SELECT u FROM User u WHERE u.enabled = false")
    List<User> findDisabledUsers();

    @Query("SELECT u FROM User u WHERE u.accountExpiryDate < :currentDate")
    List<User> findExpiredAccounts(@Param("currentDate") java.time.LocalDate currentDate);

    @Query("SELECT u FROM User u WHERE u.credentialsExpiryDate < :currentDate")
    List<User> findUsersWithExpiredCredentials(@Param("currentDate") java.time.LocalDate currentDate);

    @Query("SELECT u FROM User u WHERE u.role.roleName = com.mho_toys.backend.model.ApplicationRole.ROLE_ADMIN")
    List<User> findAllAdmins();

    @Query("SELECT u FROM User u WHERE u.role.roleName = com.mho_toys.backend.model.ApplicationRole.ROLE_USER")
    List<User> findAllCustomers();

    @Query("SELECT u FROM User u WHERE u.isTwoFactorEnabled = true")
    List<User> findUsersWithTwoFactorEnabled();

    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = 0 WHERE u.failedLoginAttempts > 0")
    int resetAllFailedLoginAttempts();

    @Modifying
    @Query("UPDATE User u SET u.accountNonLocked = true, u.accountLockedDate = null, u.failedLoginAttempts = 0 WHERE u.accountNonLocked = false")
    int unlockAllAccounts();

    @Modifying
    @Query("UPDATE User u SET u.lastLoginDate = :loginDate WHERE u.userId = :userId")
    int updateLastLoginDate(@Param("userId") Long userId, @Param("loginDate") Instant loginDate);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdDate > :since")
    long countNewUsersRegisteredSince(@Param("since") Instant since);

    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLoginDate > :since")
    long countActiveUsersSince(@Param("since") Instant since);

    @Query("SELECT COUNT(u) FROM User u WHERE u.accountNonLocked = false")
    long countLockedAccounts();

    @Query("SELECT COUNT(u) FROM User u WHERE u.enabled = false")
    long countDisabledAccounts();
} 