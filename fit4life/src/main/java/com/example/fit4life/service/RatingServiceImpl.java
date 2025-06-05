package com.example.fit4life.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.fit4life.model.Rating;
import com.example.fit4life.model.Studio;
import com.example.fit4life.model.User;
import com.example.fit4life.repository.RatingRepository;
import com.example.fit4life.repository.StudioRepository;
import com.example.fit4life.repository.UserRepository;

@Service
public class RatingServiceImpl implements RatingService {

    // Assuming you have a repository for Rating
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final StudioRepository studioRepository;
    
    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, UserRepository userRepository, StudioRepository studioRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.studioRepository = studioRepository;
    }

    @Override
    public Rating addOrUpdateRating(Long userId, Long studioId, int ratingValue) {
        // Check if the user has already rated the studio
        Rating existingRating = ratingRepository.findByUserAndStudio(userId, studioId);
        if (existingRating != null) {
            existingRating.setRatingValue(ratingValue);
            return ratingRepository.save(existingRating);
        } else {
            Optional<User> user = userRepository.findById(userId);
            Optional<Studio> studio = studioRepository.findById(studioId);
            if(user.isEmpty() || studio.isEmpty()) {
                throw new IllegalArgumentException("User or Studio not found");
            }
            if(user.get().isBanned()) {
                throw new IllegalArgumentException("User is banned and cannot rate studios");
            }
            if(ratingValue < 1 || ratingValue > 10) {
                throw new IllegalArgumentException("Rating value must be between 1 and 10");
            }
            // Create a new rating
            Rating newRating = new Rating();
            newRating.setRatingValue(ratingValue);
            newRating.setUser(user.get());
            newRating.setStudio(studio.get());
            return ratingRepository.save(newRating);
        }
    }

    @Override
    public List<Rating> getAllRatings() {
        // Retrieve all ratings
        return ratingRepository.findAll();
    }

    @Override
    public List<Rating> getRatingsByStudio(Long studioId){
        Studio studio = studioRepository.findById(studioId).orElse(null);
        if (studio != null) {
            return ratingRepository.findByStudioId(studioId);
        }else {
            return List.of(); // Return an empty list if the studio does not exist
        }
    }

    @Override
    public Rating getRatingByUserAndStudio(Long userId, Long studioId) {
        User user = userRepository.findById(userId).orElse(null);
        Studio studio = studioRepository.findById(studioId).orElse(null);
        if (user == null || studio == null) {
            return null; // Return null if user or studio does not exist
        }
        // Retrieve rating by user and studio
        return ratingRepository.findByUserAndStudio(userId, studioId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void deleteRating(Long id) {
        // Delete rating by ID
        // treba da se dodade i update na prosechna ocenka na studio, i da se izbrishe ratingot od studioto i userot
        Rating rating = ratingRepository.findById(id).orElse(null);
        if (rating == null) {
            return;
        }
        Long studioId = rating.getStudio().getId();
        User user = userRepository.findById(rating.getUser().getId()).orElse(null);
        if (user != null) {
            user.getRatings().remove(rating);
        }else{
            return; // If user does not exist, do nothing
        }
        studioRepository.findById(studioId).ifPresent(studio -> {
            studio.getRatings().remove(rating);
            studioRepository.save(studio);
        });
        updateAverageRating(studioId);
        ratingRepository.deleteById(id);
    }
    @Override
    public List<Rating> getRatingsByUser(Long userId) {
        return ratingRepository.findByUserId(userId);
    }

    private void updateAverageRating(Long studioId) {
        Studio studio = studioRepository.findById(studioId).orElse(null);
        if (studio == null) {
            return;
        }
        List<Rating> ratings = studio.getRatings();
        if (ratings == null || ratings.isEmpty()) {
            studio.setAverageRating(0.0);
            return;
        }
        double sum = 0.0;
        for (Rating rating : ratings) {
            sum += rating.getRatingValue();
        }
        studio.setAverageRating(sum / ratings.size());
        studioRepository.save(studio);
    }
}
