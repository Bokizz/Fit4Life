package com.example.fit4life.service;
import java.util.List;

import com.example.fit4life.model.User;

public interface UserService {
    User createUser(User user);
    User updateUser(Long id, String username);
    User getUserById(Long id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    void banUser(Long id);
    void unbanUser(Long id);
    void restrictChat(Long id);
    void unrestrictChat(Long id);
    void deleteUser(Long id);
    User getCurrentUser();

}   
