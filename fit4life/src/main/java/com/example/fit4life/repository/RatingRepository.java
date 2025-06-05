package com.example.fit4life.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fit4life.model.Rating;
public interface RatingRepository extends JpaRepository<Rating, Long> {
    // Additional query methods can be defined here if needed
    // For example, to find ratings by user and studio:
    Rating findByUserAndStudio(Long userId, Long studioId);
    void deleteByUserId(Long userId);
    // 🔍 Find all ratings for a specific studio
    List<Rating> findByStudioId(Long studioId);
    List<Rating> findByUserId(Long userId);
}
