package com.example.fit4life.service;

import java.util.List;

import com.example.fit4life.model.Photo;
public interface PhotoService {
    Photo uploadPhoto(Photo photo, Long userId, Long studioId, String url, String description);
    Photo updatePhoto(Photo photo, String url, String description);
    List<Photo> getPhotosByStudio(Long studioId);
    Photo getPhotoById(Long photoId);
    List<Photo> getPhotosByUploadedBy(Long userId);
    List<Photo> getAllPhotos();
    void deletePhoto(Long photoId);
}