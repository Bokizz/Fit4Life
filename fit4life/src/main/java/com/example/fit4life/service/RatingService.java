package com.example.fit4life.service;
import java.util.List;

import com.example.fit4life.model.Rating;
import com.example.fit4life.model.Studio;
import com.example.fit4life.model.User;

public interface RatingService {
    Rating addOrUpdateRating(User user, Studio studio, int RatingValue);
    List<Rating> getAllRatings();
    List<Rating> getRatingsByStudio(Long studioId);
    Rating getRatingByUserAndStudio(Long userId, Long studioId);
    void deleteRating(Long id);
}
