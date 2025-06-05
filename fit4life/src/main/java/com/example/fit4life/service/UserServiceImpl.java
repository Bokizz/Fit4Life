package com.example.fit4life.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.fit4life.model.User;
import com.example.fit4life.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, String username) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setUsername(username);
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void banUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBanned(true);
        user.getComments().clear();
        user.getRatings().clear();
        user.getSubscriptions().clear();
        user.getUploadedPhotos().clear();
        userRepository.save(user);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void unbanUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBanned(false);
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Override
    public void restrictChat(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setChatRestricted(true);
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Override
    public void unrestrictChat(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setChatRestricted(false);
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (getCurrentUser().getId().equals(id)) {
            throw new IllegalArgumentException("Cannot delete your own account");
        }
        userRepository.delete(user);
    }

    @Override
    public User getCurrentUser() {
         String username = SecurityContextHolder.getContext().getAuthentication().getName();
         User user = userRepository.findByUsername(username);
         if (user == null) {
             throw new UsernameNotFoundException("Current user not found");
         }
         return user;
    }
}

