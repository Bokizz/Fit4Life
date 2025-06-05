package com.example.fit4life.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fit4life.model.Studio;
import com.example.fit4life.model.enumeration.StudioType;

public interface StudioRepository extends JpaRepository<Studio, Long> {
    List<Studio> findByName(String name);
    List<Studio> findByNameContainingIgnoreCase(String keyword);
    List<Studio> findByLocation(String location);
    List<Studio> findByType(StudioType type);
    List<Studio> findByAverageRatingGreaterThanEqual(double rating);
    List<Studio> findByLocationAndType(String location, StudioType type);
}
