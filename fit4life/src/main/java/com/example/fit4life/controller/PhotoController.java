package com.example.fit4life.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fit4life.model.Photo;
import com.example.fit4life.model.Studio;
import com.example.fit4life.model.User;
import com.example.fit4life.service.PhotoService;
import com.example.fit4life.service.StudioService;
import com.example.fit4life.service.UserService;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    // This class will handle HTTP requests related to photos
    // It will use PhotoService to perform operations like upload, delete, and fetch photos
    // Methods will be added here as needed for the application functionality
    // Example methods could include:
    // - uploadPhoto(Photo photo)
    // - getPhotosByStudio(Long studioId)
    // - deletePhoto(Long photoId)
    // - getAllPhotos()
    // Note: Actual implementation will depend on the specific requirements and design of the application
    // Placeholder for future methods and logic
    @Autowired
    private PhotoService photoService;
    
    @Autowired
    private UserService userService; // Assuming you have a UserService for user-related operations
    
    @Autowired
    private StudioService studioService; // Assuming you have a StudioService for studio-related operations
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public Photo uploadPhoto(@RequestBody Photo photo) {
        // Validate studio exists
        Studio studio = studioService.getStudioById(photo.getStudio().getId());
        if (studio == null) {
            throw new RuntimeException("Studio not found");
        }

        // Get the current authenticated user
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);

        // Set the uploadedBy field
        photo.setUploadedBy(currentUser);
        // Set the studio field
        photo.setStudio(studio);

        // Save the photo
        return photoService.uploadPhoto(photo);
    }

    // Fetch photos by studio ID
    @GetMapping("/studio/{studioId}")
    public List<Photo> getPhotosByStudio(@PathVariable Long studioId) {
        return photoService.getPhotosByStudio(studioId);
    }

    // Delete the photo
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{photoId}")
    public void deletePhoto(@PathVariable Long photoId) {
        photoService.deletePhoto(photoId);
    }
}
