package com.example.fit4life.service;
import java.util.List;

import com.example.fit4life.model.Studio;
import com.example.fit4life.model.enumeration.StudioType;

public interface StudioService {
    Studio createStudio(Studio studio);
    Studio updateStudio(Long id, String name, String locatio);
    Studio getStudioById(Long id);
    List<Studio> getAllStudios();  
    List<Studio> searchByName(String keyword);
    List<Studio> searchByLocation(String location);
    List<Studio> searchByType(StudioType type);
    List<Studio> searchByAverageRating(double rating);
    List<Studio> searchByLocationAndType(String location, StudioType type);
    void deleteStudio(Long id);
}
