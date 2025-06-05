package com.example.fit4life.service;
import java.util.List;

import com.example.fit4life.model.Rating;

public interface RatingService {
    Rating addOrUpdateRating(Long userId, Long studioId, int ratingValue);
    List<Rating> getAllRatings();
    List<Rating> getRatingsByStudio(Long studioId);
    Rating getRatingByUserAndStudio(Long userId, Long studioId);
    void deleteRating(Long id);
}
