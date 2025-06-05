package com.example.fit4life.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fit4life.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // Custom query methods can be added here if needed
    List<Subscription> findByUserId(Long userId);
    List<Subscription> findByStudioId(Long studioId);
    Subscription findByUserIdAndStudioId(Long userId, Long studioId);
    void deleteById(Long id);
}
