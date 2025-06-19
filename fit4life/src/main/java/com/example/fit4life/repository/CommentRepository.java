package com.example.fit4life.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fit4life.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUser_IdAndStudio_Id(Long userId, Long studioId);
    List<Comment> findByUser_Id(Long userId);
    List<Comment> findByStudio_Id(Long studioId);    
}
