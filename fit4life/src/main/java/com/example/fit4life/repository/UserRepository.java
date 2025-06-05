package com.example.fit4life.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fit4life.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods can be added here if needed
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    // Additional methods for user management can be added here
    // For example, to find users by role, status, etc.
    // List<User> findByRole(Role role);
    // List<User> findByBanned(boolean banned);
    
}
