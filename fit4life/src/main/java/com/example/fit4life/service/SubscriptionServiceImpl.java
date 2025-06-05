package com.example.fit4life.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.fit4life.model.Subscription;
import com.example.fit4life.repository.StudioRepository;
import com.example.fit4life.repository.SubscriptionRepository;
import com.example.fit4life.repository.UserRepository;
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    // Autowired dependencies
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final StudioRepository studioRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, UserRepository userRepository, StudioRepository studioRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.studioRepository = studioRepository;
    }

    @Override
    public Subscription subscribeToStudio(Long userId, Long studioId, int duration) {
        // Validate user and studio existence
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        if (!studioRepository.existsById(studioId)) {
            throw new IllegalArgumentException("Studio not found");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }
        if(userRepository.findById(userId).orElseThrow().isBanned()) {
            throw new IllegalArgumentException("User is banned and cannot subscribe to studios");
        }

        // Create a new subscription
        Subscription subscription = new Subscription();
        subscription.setUser(userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found")));
        subscription.setStudio(studioRepository.findById(studioId).orElseThrow(() -> new IllegalArgumentException("Studio not found")));
        subscription.setDuration(duration);

        return subscriptionRepository.save(subscription);
    }

    @Override
    public void cancelSubscription(Long userId, Long studioId){
        Subscription subscription = subscriptionRepository.findByUserIdAndStudioId(userId, studioId);
        if (subscription != null) {
            subscriptionRepository.delete(subscription);
            userRepository.findById(userId).ifPresent(user -> {
                user.getSubscriptions().remove(subscription);
                userRepository.save(user);
            });
            studioRepository.findById(studioId).ifPresent(studio -> {
                studio.getSubscriptions().remove(subscription);
                studioRepository.save(studio);
            });
        } else {
            throw new IllegalArgumentException("Subscription not found");
        }
    }

    @Override
    public Subscription getSubscriptionByUserAndStudio(Long userId, Long studioId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        if (!studioRepository.existsById(studioId)) {
            throw new IllegalArgumentException("Studio not found");
        }
        return subscriptionRepository.findByUserIdAndStudioId(userId, studioId);
    }

    @Override
    public List<Subscription> getSubscriptionsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public List<Subscription> getSubscriptionsByStudio(Long studioId) {
        if (!studioRepository.existsById(studioId)) {
            throw new IllegalArgumentException("Studio not found");
        }
        return subscriptionRepository.findByStudioId(studioId);
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public void deleteSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
        subscriptionRepository.delete(subscription);
    }
}
