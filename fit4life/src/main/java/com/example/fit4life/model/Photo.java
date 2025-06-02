package com.example.fit4life.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String description;
    
    @ManyToOne(optional = false)
    private Studio studio;

    @ManyToOne(optional = false)
    private User uploadedBy; // Only admins can upload

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Studio getStudio() {
        return studio;
    }
    public void setStudio(Studio studio) {
        this.studio = studio;
    }
    public User getUploadedBy() {
        return uploadedBy;
    }
    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
