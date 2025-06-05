package com.example.fit4life.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fit4life.model.Rating;
import com.example.fit4life.model.Studio;
import com.example.fit4life.model.enumeration.StudioType;
import com.example.fit4life.repository.StudioRepository;

@Service
public class StudioServiceImpl implements StudioService {

    // Autowired dependencies
    private final StudioRepository studioRepository;

    @Autowired
    public StudioServiceImpl(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    @Override
    public Studio createStudio(Studio studio) {
        // Validate and save the studio
        return studioRepository.save(studio);
    }

    @Override
    public Studio updateStudio(Long id, String name, String location) {
        // Validate and update the studio
        Studio existingStudio = studioRepository.findById(id).orElse(null);
        if (existingStudio != null) {
            existingStudio.setName(name);
            existingStudio.setLocation(location);
            updateAverageRating(existingStudio);
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
    }

}
