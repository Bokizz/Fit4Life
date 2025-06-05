package com.example.fit4life.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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

import com.example.fit4life.model.Photo;
import com.example.fit4life.service.PhotoService;
@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public Photo uploadPhoto(@RequestBody Photo photo, @PathVariable Long userId, @PathVariable Long studioId,
                             @RequestParam String url, @RequestParam String description) {
        return photoService.uploadPhoto(photo, userId, studioId, url, description);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{photoId}")
    public Photo updatePhoto(@PathVariable Long photoId, @RequestBody Photo photo,
                             @RequestParam String url, @RequestParam String description) throws NotFoundException {
        Photo existingPhoto = photoService.getPhotoById(photoId);
        if (existingPhoto == null) {
            throw new NotFoundException();
        }
        return photoService.updatePhoto(photo, url, description);
    }

    @GetMapping("/studio/{studioId}")
    public List<Photo> getPhotosByStudio(@PathVariable Long studioId) {
        return photoService.getPhotosByStudio(studioId);
    }

    @GetMapping("/{photoId}")
    public Photo getPhotoById(@PathVariable Long photoId) {
        return photoService.getPhotoById(photoId);
    }

    @GetMapping("/user/{userId}")
    public List<Photo> getPhotosByUploadedBy(@PathVariable Long userId) {
        return photoService.getPhotosByUploadedBy(userId);
    }
    
    @GetMapping("/all/photos")
    public List<Photo> getAllPhotos() {
        return photoService.getAllPhotos();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{photoId}")
    public void deletePhoto(@PathVariable Long photoId) {
        photoService.deletePhoto(photoId);
    }

}
