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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fit4life.model.Studio;
import com.example.fit4life.model.enumeration.StudioType;
import com.example.fit4life.service.StudioService;

@RestController
@RequestMapping("/api/studios")
public class StudioController {
    private final StudioService studioService;

    @Autowired
    public StudioController(StudioService studioService){
        this.studioService = studioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Studio> createStudio(@RequestBody Studio studio) {
        // Validate and save the studio
        Studio createdStudio = studioService.createStudio(studio);
        return ResponseEntity.ok(createdStudio);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Studio> updateStudio(@PathVariable Long id, @RequestParam String name, @RequestParam String location) {
        // Validate and update the studio
        Studio updatedStudio = studioService.updateStudio(id, name, location);
        if (updatedStudio != null) {
            return ResponseEntity.ok(updatedStudio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Studio> getStudioById(@PathVariable Long id) {
        // Retrieve studio by ID
        Studio studio = studioService.getStudioById(id);
        if (studio != null) {
            return ResponseEntity.ok(studio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Studio>> getAllStudios() {
        List<Studio> studios = studioService.getAllStudios();
        return ResponseEntity.ok(studios);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStudio(@PathVariable Long id) {
        // Delete studio by ID
        studioService.deleteStudio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Studio>> searchByName(@RequestParam String name) {
        List<Studio> studios = studioService.searchByName(name);
        return ResponseEntity.ok(studios);
    }

    @GetMapping("/search/location")
    public ResponseEntity<List<Studio>> searchByLocation(@RequestParam String location) {
        List<Studio> studios = studioService.searchByLocation(location);
        return ResponseEntity.ok(studios);
    }

    @GetMapping("/search/type")
    public ResponseEntity<List<Studio>> searchByType(@RequestParam String type) {
        List<Studio> studios = studioService.searchByType(StudioType.valueOf(type.toUpperCase()));
        return ResponseEntity.ok(studios);
    }

    @GetMapping("/search/rating")
    public ResponseEntity<List<Studio>> searchByAverageRating(@RequestParam Double rating) {
        List<Studio> studios = studioService.searchByAverageRating(rating);
        return ResponseEntity.ok(studios);
    }

    @GetMapping("/search/location-and-type")
    public ResponseEntity<List<Studio>> searchByLocationAndType(@RequestParam String location, @RequestParam String type) {
        List<Studio> studios = studioService.searchByLocationAndType(location, StudioType.valueOf(type.toUpperCase()));
        return ResponseEntity.ok(studios);
    }

}
