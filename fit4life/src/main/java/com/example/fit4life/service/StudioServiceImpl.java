package com.example.fit4life.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fit4life.model.Rating;
import com.example.fit4life.model.Studio;
import com.example.fit4life.model.enumeration.StudioType;
import com.example.fit4life.repository.RatingRepository;
import com.example.fit4life.repository.StudioRepository;

@Service
public class StudioServiceImpl implements StudioService {

    // Autowired dependencies
    private final StudioRepository studioRepository;

    private final RatingRepository ratingRepository;
    @Autowired
    public StudioServiceImpl(StudioRepository studioRepository,RatingRepository ratingRepository) {
        this.studioRepository = studioRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Studio createStudio(Studio studio) {
        studio.setAverageRating(0.0);
        return studioRepository.save(studio);
    }

    @Override
    public Studio updateStudio(Long id, String name, String location) {
        // Validate and update the studio
        Studio existingStudio = studioRepository.findById(id).orElse(null);
        if (existingStudio != null) {
            existingStudio.setName(name);
            existingStudio.setLocation(location);
            updateAverageRating(existingStudio.getId());
            return studioRepository.save(existingStudio);
        }
        return null;
    }
    @Override
    public Studio getStudioById(Long id) {
        // Retrieve studio by ID
        return studioRepository.findById(id).orElse(null);
    }
    @Override
    public List<Studio> getAllStudios() {
        // Retrieve all studios
        return studioRepository.findAll();
    }

    @Override
    public List<Studio> searchByAverageRating(double rating){
        return studioRepository.findByAverageRatingGreaterThanEqual(rating);
    }

    @Override
    public List<Studio> searchByLocation(String location) {
        return studioRepository.findByLocation(location);
    }

    @Override
    public List<Studio> searchByType(StudioType type) {
        return studioRepository.findByType(type);
    }

    @Override
    public List<Studio> searchByLocationAndType(String location, StudioType type) {
        return studioRepository.findByLocationAndType(location, type);
    }

    @Override
    public List<Studio> searchByName(String name) {
        return studioRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public void deleteStudio(Long id) {
        studioRepository.deleteById(id);
    }
    // private void updateAverageRating(Studio studio) {
    //     List<Rating> ratings = studio.getRatings();
    //     if (ratings == null || ratings.isEmpty()) {
    //         Rating rating = new Rating();
    //         rating.setStudio(studio);
    //         rating.setRatingValue(0);
    //         studio.getRatings().add(rating);
    //         studio.setAverageRating(0.0);
    //         return;
    //     }
    //     double sum = 0.0;
    //     for (Rating rating : ratings) {
    //         sum += rating.getRatingValue();
    //     }
    //     studio.setAverageRating(sum / ratings.size());
    // }

    private void updateAverageRating(Long studioId) {
    Studio studio = studioRepository.findById(studioId)
        .orElseThrow(() -> new IllegalArgumentException("Studio not found"));
    
    List<Rating> ratings = ratingRepository.findByStudio_Id(studioId);
    
    if (ratings.isEmpty()) {
        studio.setAverageRating(0.0);
    } else {
        double sum = ratings.stream()
            .mapToInt(Rating::getRatingValue)
            .average()
            .orElse(0.0);
        studio.setAverageRating(sum);
    }
    
    studioRepository.save(studio);
}
}
