package com.example.fit4life.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fit4life.model.User;
import com.example.fit4life.service.CommentService;
import com.example.fit4life.service.PhotoService;
import com.example.fit4life.service.RatingService;
import com.example.fit4life.service.SubscriptionService;
import com.example.fit4life.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final RatingService ratingService;
    private final CommentService commentService;
    private final PhotoService photoService;

    @Autowired
    public UserController(UserService userService, SubscriptionService subscriptionService, RatingService ratingService, CommentService commentService, PhotoService photoService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.ratingService = ratingService;
        this.commentService = commentService;
        this.photoService = photoService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/update/{userId}/{username}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @PathVariable String username) {
        User updatedUser = userService.updateUser(userId, username);
        return ResponseEntity.ok(updatedUser);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = userService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ban/{userId}")
    public ResponseEntity<Void> banUser(@PathVariable Long userId) {
        subscriptionService.getSubscriptionsByUser(userId).forEach(subscription -> {
            subscriptionService.cancelSubscription(userId, subscription.getStudio().getId());
        });
        ratingService.getRatingsByUser(userId).forEach(rating -> {
            ratingService.deleteRating(rating.getId());
        });
        commentService.getCommentsByUser(userId).forEach(comment -> {
            commentService.deleteComment(comment.getId());
        });
        photoService.getPhotosByUploadedBy(userId).forEach(photo -> {
            photoService.deletePhoto(photo.getId());
        });
        userService.banUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/unban/{userId}")
    public ResponseEntity<Void> unbanUser(@PathVariable Long userId) {
        userService.unbanUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @PostMapping("/restrict-chat/{userId}")
    public ResponseEntity<Void> restrictChat(@PathVariable Long userId) {
        userService.restrictChat(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @PostMapping("/unrestrict-chat/{userId}")
    public ResponseEntity<Void> unrestrictChat(@PathVariable Long userId) {
        userService.unrestrictChat(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
