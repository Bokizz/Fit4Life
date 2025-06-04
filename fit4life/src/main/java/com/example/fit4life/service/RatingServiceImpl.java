package com.example.fit4life.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fit4life.model.Rating;
import com.example.fit4life.repository.RatingRepository;    
import com.example.fit4life.repository.UserRepository;
import com.example.fit4life.repository.StudioRepository;

import java.util.List;

import com.example.fit4life.model.User;
import com.example.fit4life.model.Studio;

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
    public Rating addOrUpdateRating(User user, Studio studio, int ratingValue) {
        // Check if the user has already rated the studio
        Rating existingRating = ratingRepository.findByUserAndStudio(user.getId(), studio.getId());
        if (existingRating != null) {
            existingRating.setRatingValue(ratingValue);
            return ratingRepository.save(existingRating);
        } else {
            // Create a new rating
            Rating newRating = new Rating();
            newRating.setUser(user);
            newRating.setStudio(studio);
            newRating.setRatingValue(ratingValue);
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

    @Override
    public void deleteRating(Long id) {
        // Delete rating by ID
        ratingRepository.deleteById(id);
    }

    private void updateAverageRating(Studio studio) {
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
