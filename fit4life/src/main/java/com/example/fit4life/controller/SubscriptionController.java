package com.example.fit4life.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fit4life.model.Subscription;
import com.example.fit4life.service.StudioService;
import com.example.fit4life.service.SubscriptionService;
import com.example.fit4life.service.UserService;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final StudioService studioService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, UserService userService, StudioService studioService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
        this.studioService = studioService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Subscription> subscribeToStudio(@RequestParam Long userId, @RequestParam Long studioId, @RequestParam int duration) {
        Subscription subscription = subscriptionService.subscribeToStudio(userId, studioId, duration);
        return ResponseEntity.ok(subscription);
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancelSubscription(@RequestParam Long userId, @RequestParam Long studioId) {
        subscriptionService.cancelSubscription(userId, studioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByUser(@PathVariable Long userId) {
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByUser(userId);
        return ResponseEntity.ok(subscriptions);
    }
    @GetMapping("/studio/{studioId}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByStudio(@PathVariable Long studioId) {
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByStudio(studioId);
        return ResponseEntity.ok(subscriptions);
    }
    @GetMapping("/user-studio")
    public ResponseEntity<Subscription> getSubscriptionByUserAndStudio(@RequestParam Long userId, @RequestParam Long studioId) {
        Subscription subscription = subscriptionService.getSubscriptionByUserAndStudio(userId, studioId);
        if (subscription != null) {
            return ResponseEntity.ok(subscription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }
    
}
