package com.example.fit4life.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fit4life.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

