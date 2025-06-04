package com.example.fit4life.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fit4life.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Method to find comments by user ID and studio ID
    List<Comment> findByUserIdAndStudioId(Long userId, Long studioId);

    // Method to find comments by user ID
    List<Comment> findByUserId(Long userId);
    
    List<Comment> findByStudioId(Long studioId);
    
    // Method to delete comments by user ID
    void deleteByUserId(Long userId);
    
}
