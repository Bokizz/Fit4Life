package com.example.fit4life.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fit4life.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUser_Id(Long userId);
    List<Subscription> findByStudio_Id(Long studioId);
    Subscription findByUser_IdAndStudio_Id(Long userId, Long studioId);
}
