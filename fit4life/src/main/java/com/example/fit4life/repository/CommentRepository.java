package com.example.fit4life.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fit4life.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserIdAndStudioId(Long userId, Long studioId);
    List<Comment> findByUserId(Long userId);
    List<Comment> findByStudioId(Long studioId);
    void deleteByUserId(Long userId);
    
}
