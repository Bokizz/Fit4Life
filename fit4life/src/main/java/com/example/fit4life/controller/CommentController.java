package com.example.fit4life.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fit4life.model.Comment;
import com.example.fit4life.model.Studio;
import com.example.fit4life.model.User;
import com.example.fit4life.service.CommentService;
import com.example.fit4life.service.StudioService;
import com.example.fit4life.service.UserService;


@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final StudioService studioService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, StudioService studioService) {
        this.commentService = commentService;
        this.userService = userService;
        this.studioService = studioService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<Comment> addComment(@RequestParam String content,@RequestParam Long userId, @RequestParam Long studioId) {
        User user = userService.getUserById(userId);
        Studio studio = studioService.getStudioById(studioId);
        if (user == null || studio == null) {
            return ResponseEntity.badRequest().build();
        }
        if (user.isChatRestricted()) {
            return ResponseEntity.badRequest().body(null); // User is restricted from commenting
        }
        Comment createdComment = commentService.addComment(content, userId, studioId);
        return ResponseEntity.ok(createdComment);
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestParam String content) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build(); // Comment not found
        }
        User currentUser = userService.getCurrentUser();
        if (currentUser == null || !currentUser.getId().equals(comment.getUser().getId())) {
            return ResponseEntity.status(403).build(); // Forbidden if not the owner of the comment
        }
        Comment updatedComment = commentService.updateComment(commentId, content);
        if (updatedComment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedComment);
    }

    @GetMapping("/studio/{studioId}")
    public ResponseEntity<List<Comment>> getCommentsByStudio(@PathVariable Long studioId) {
        List<Comment> comments = commentService.getCommentsByStudio(studioId);
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.notFound().build(); // No comments found for the studio
        }
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build(); // Comment not found
        }
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUser(@PathVariable Long userId) {
        List<Comment> comments = commentService.getCommentsByUser(userId);
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.notFound().build(); // No comments found for the user
        }
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.notFound().build(); // No comments found
        }
        return ResponseEntity.ok(comments);
    }
    @GetMapping("/user/{userId}/studio/{studioId}")
    public ResponseEntity<List<Comment>> getCommentsByUserAndStudio(@PathVariable Long userId, @PathVariable Long studioId) {
        List<Comment> comments = commentService.getCommentsByUserAndStudio(userId, studioId);
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.notFound().build(); // No comments found for the user and studio
        }
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/delete/{commentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build(); // Comment not found
        }
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted!"); // Successfully deleted
    }


}
