package com.example.fit4life.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fit4life.model.Rating;
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT r FROM Rating r WHERE r.user.id = :userId AND r.studio.id = :studioId")
    Rating findByUserAndStudio(@Param("userId") Long userId, @Param("studioId") Long studioId);
    void deleteByUserId(Long userId);
    List<Rating> findByStudioId(Long studioId);
    List<Rating> findByUserId(Long userId);
}
