package com.example.fit4life.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.fit4life.model.Studio;
import com.example.fit4life.model.User;
import com.example.fit4life.repository.StudioRepository;
import com.example.fit4life.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StudioRepository studioRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, StudioRepository studioRepository) {
        this.userRepository = userRepository;
        this.studioRepository = studioRepository;
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

    // @Override
    // public Studio createStudio(Studio studio){
    //     if(studioRepository.existsById(studio.getId())){
    //         throw new IllegalArgumentException("Studio already exists");
    //     }
    //     if(studioRepository.findByName(studio.getName()) != null){
    //         throw new IllegalArgumentException("Studio name already exists");
    //     }
    //     studio.setAverageRating(0.0);
    //     return studioRepository.save(studio);
    // }

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
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

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
    
    @Override
    public void unbanUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBanned(false);
        userRepository.save(user);
    }

    @Override
    public void restrictChat(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if ("ADMIN".equals(user.getRole()) || "MODERATOR".equals(user.getRole())) {
            throw new IllegalArgumentException("Cannot chat restrict admins or moderators!");
        }else{
            user.setChatRestricted(true);
            userRepository.save(user);
        }
        
    }

    @Override
    public void unrestrictChat(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setChatRestricted(false);
        userRepository.save(user);
    }

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
         User user = userRepository.findByUsername(username)
                 .orElseThrow(() -> new UsernameNotFoundException("Current user not found"));
         return user;
    }
    
    @Override
    public void deleteStudio(Long studioId){
        Studio studio = studioRepository.findById(studioId).orElseThrow(() -> new IllegalArgumentException("Studio not found"));
        studioRepository.delete(studio);
    }
}

