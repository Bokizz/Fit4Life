package com.example.fit4life.service;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.fit4life.model.Comment;
import com.example.fit4life.model.Studio;
import com.example.fit4life.model.User;
import com.example.fit4life.repository.CommentRepository;
import com.example.fit4life.repository.StudioRepository;
import com.example.fit4life.repository.UserRepository;
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
            if(user.isBanned()) {
                throw new IllegalArgumentException("User is banned and cannot comment");
            }
                if(user.isChatRestricted()) {
            throw new IllegalArgumentException("User is restricted from commenting");
        } else {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setStudio(studio);
            return commentRepository.save(comment);
        }
    }

    @Override
    public Comment updateComment(Long id,String content){
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        if (existingComment.isEdited()) {
            throw new IllegalArgumentException("Comment has already been edited");
        }
        if(existingComment.getUser().isChatRestricted() ) {
            throw new IllegalArgumentException("User is restricted from editing comments");
        }
        if (existingComment.getUser().isBanned()) {
            throw new IllegalArgumentException("User is banned and cannot edit comments");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        if (content.length() > 100) {
            throw new IllegalArgumentException("Content exceeds maximum length of 100 characters");
        }
        existingComment.setContent(content);
        existingComment.setEditedAt(LocalDateTime.now());
        existingComment.setEdited(true);
        return commentRepository.save(existingComment);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
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

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getCommentsByUserAndStudio(Long userId, Long studioId) {
        return commentRepository.findByUserIdAndStudioId(userId, studioId);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
            commentRepository.delete(comment);
    }

}