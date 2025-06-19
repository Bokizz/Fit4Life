package com.example.fit4life.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fit4life.model.Photo;
import com.example.fit4life.model.Studio;
import com.example.fit4life.model.User;
import com.example.fit4life.repository.PhotoRepository;
import com.example.fit4life.repository.StudioRepository;
import com.example.fit4life.repository.UserRepository;

@Service
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final StudioRepository studioRepository;
    
    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, UserRepository userRepository, StudioRepository studioRepository){
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.studioRepository = studioRepository;
    }

    @Override
    public Photo uploadPhoto(Photo photo){
        User user = userRepository.findById(photo.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Studio studio = studioRepository.findById(photo.getStudioId()).orElseThrow(() -> new RuntimeException("Studio not found"));
        photo.setUploadedBy(user);
        photo.setStudio(studio);
        return photoRepository.save(photo);
    }

    @Override
    public Photo updatePhoto(Long photoId, String url, String description){
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        photo.setUrl(url);
        photo.setDescription(description);
        return photoRepository.save(photo);
    }

    @Override
    public List<Photo> getPhotosByStudio(Long studioId){
        studioRepository.findById(studioId).orElseThrow(() -> new RuntimeException("Studio not found"));
        return photoRepository.findAll().stream()
                .filter(photo -> photo.getStudio().getId().equals(studioId))
                .toList();
    }

    @Override
    public Photo getPhotoById(Long photoId){
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found!"));
    }

    @Override
    public List<Photo> getPhotosByUploadedBy(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return photoRepository.findAll().stream()
                .filter(photo -> photo.getUploadedBy().getId().equals(user.getId()))
                .toList();
    }

    @Override
    public List<Photo> getAllPhotos(){
        return photoRepository.findAll();
    }

    @Override
    public void deletePhoto(Long photoId){
        photoRepository.deleteById(photoId);
    }

}
