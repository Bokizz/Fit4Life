package com.example.fit4life.service;

import java.util.List;

import com.example.fit4life.model.Subscription;
public interface SubscriptionService{
    Subscription subscribeToStudio(Long userId, Long studioId,int duration);
    void cancelSubscription(Long userId, Long studioId);
    Subscription getSubscriptionByUserAndStudio(Long userId, Long studioId);
    List<Subscription> getSubscriptionsByUser(Long userId);
    List<Subscription> getSubscriptionsByStudio(Long studioId);
    List<Subscription> getAllSubscriptions();
}
