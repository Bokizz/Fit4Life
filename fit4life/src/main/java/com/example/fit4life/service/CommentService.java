package com.example.fit4life.service;
import java.util.List;

import com.example.fit4life.model.Comment;

public interface CommentService {
    Comment addComment(String content, Long userId, Long studioId);
    Comment updateComment(Long id, String content);
    Comment getCommentById(Long id);
    List<Comment> getCommentsByStudio(Long studioId);
    List<Comment> getCommentsByUser(Long userId);
    List<Comment> getAllComments();
    List<Comment> getCommentsByUserAndStudio(Long userId, Long studioId);
    void deleteComment(Long id);
}
