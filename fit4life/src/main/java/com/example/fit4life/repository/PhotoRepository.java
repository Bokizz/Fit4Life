package com.example.fit4life.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fit4life.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long>{
    
}
