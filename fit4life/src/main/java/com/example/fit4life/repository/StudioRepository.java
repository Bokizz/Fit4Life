package com.example.fit4life.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fit4life.model.Studio;
import com.example.fit4life.model.enumeration.StudioType;

public interface StudioRepository extends JpaRepository<Studio, Long> {
    // Additional query methods can be defined here if needed
    // For example, to find studios by name:
    List<Studio> findByName(String name);

    // 🔍 Search by name containing keyword (case-insensitive)
    List<Studio> findByNameContainingIgnoreCase(String keyword);

    // 🔍 Search by location (exact match)
    List<Studio> findByLocation(String location);

    // 🔍 Search by type (enum)
    List<Studio> findByType(StudioType type);

    // 🔍 Search for studios with averageRating >= given value
    List<Studio> findByAverageRatingGreaterThanEqual(double rating);

    // 🔍 Combine criteria if needed:
    List<Studio> findByLocationAndType(String location, StudioType type);
    
}
