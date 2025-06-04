package com.example.fit4life.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fit4life.service.RatingService;
import com.example.fit4life.service.StudioService;
import com.example.fit4life.service.UserService;
import com.example.fit4life.model.Rating;
import com.example.fit4life.model.Studio;
import com.example.fit4life.model.User;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final UserService userService;
    private final StudioService studioService;

    @Autowired
    public RatingController(RatingService ratingService, UserService userService, StudioService studioService) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.studioService = studioService;
    }

    @PostMapping("/add")
    public ResponseEntity<Rating> addOrUpdateRating(@RequestParam Long userId, @RequestParam Long studioId, @RequestParam int ratingValue) {
        User user = userService.getUserById(userId);
        Studio studio = studioService.getStudioById(studioId);

        if (user == null || studio == null) {
            return ResponseEntity.badRequest().build();
        }
        if (ratingValue < 1 || ratingValue > 10) {
            return ResponseEntity.badRequest().build();
        }


        Rating rating = ratingService.addOrUpdateRating(user, studio, ratingValue);
        return ResponseEntity.ok(rating);
    }
    
    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingService.getAllRatings();
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/studio/{studioId}")
    public ResponseEntity<List<Rating>> getRatingsByStudio(@PathVariable Long studioId) {
        List<Rating> ratings = ratingService.getRatingsByStudio(studioId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/user/{userId}/studio/{studioId}")
    public ResponseEntity<Rating> getRatingByUserAndStudio(@RequestParam Long userId, @RequestParam Long studioId) {
        Rating rating = ratingService.getRatingByUserAndStudio(userId, studioId);
        if (rating != null) {
            return ResponseEntity.ok(rating);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}