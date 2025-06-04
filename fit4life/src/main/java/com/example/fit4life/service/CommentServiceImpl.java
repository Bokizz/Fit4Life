package com.example.fit4life.service;
import java.time.LocalDateTime;
import java.util.List;

import com.example.fit4life.model.Comment;
import com.example.fit4life.model.Studio;
import com.example.fit4life.model.User;
import com.example.fit4life.repository.CommentRepository;
import com.example.fit4life.repository.StudioRepository;
import com.example.fit4life.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.fit4life.service.CommentService;
import com.example.fit4life.model.Comment;
import com.example.fit4life.model.User;
@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final StudioRepository studioRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, StudioRepository studioRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.studioRepository = studioRepository;
    }

    @Override
    public Comment addComment(String content, Long userId, Long studioId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Studio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new IllegalArgumentException("Studio not found"));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setStudio(studio);
        if(user.isChatRestricted()) {
            throw new IllegalArgumentException("User is restricted from commenting");
        } else {
            return commentRepository.save(comment);
        }
    }

    @Override
    public Comment updateComment(Long id,String content){
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        existingComment.setContent(content);
        existingComment.setEditedAt(LocalDateTime.now());
        existingComment.setEdited(true);
        return commentRepository.save(existingComment);
    }
    @Override
    public List<Comment> getCommentsByStudio(Long studioId) {
        Studio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new IllegalArgumentException("Studio not found"));
        return commentRepository.findByStudioId(studioId);
    }

    @Override
    public List<Comment> getCommentsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return commentRepository.findByUserId(userId);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
            commentRepository.delete(comment);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getCommentsByUserAndStudio(Long userId, Long studioId) {
        return commentRepository.findByUserIdAndStudioId(userId, studioId);
    }
    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }
}