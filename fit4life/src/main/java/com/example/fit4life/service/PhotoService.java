package com.example.fit4life.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fit4life.model.Photo;
import com.example.fit4life.repository.PhotoRepository;
@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo uploadPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public List<Photo> getPhotosByStudio(Long studioId) {
        return photoRepository.findAll().stream()
                .filter(photo -> photo.getStudio().getId().equals(studioId))
                .collect(Collectors.toList());
    }

    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
    }
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }
}