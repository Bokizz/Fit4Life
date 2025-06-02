package com.example.fit4life.model;


import com.example.fit4life.model.enumeration.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false, unique = true)
private String username;

@Column(nullable = false, unique = true)
private String email;

@Column(nullable = false)
private String password;

@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Role role;

// Relationships

@OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
private List<Comment> comments = new ArrayList<>();

@OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
private List<Rating> ratings = new ArrayList<>();

@OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
private List<Subscription> subscriptions = new ArrayList<>();

@OneToMany(mappedBy = "uploadedBy", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Photo> uploadedPhotos = new ArrayList<>();

// Additional fields for user status
private boolean banned = false;
private boolean chatRestricted = false;

// Getters and Setters
public Long getId() {
    return id; 
}

public String getUsername() {
    return username;
}
public void setUsername(String username) {
    this.username = username;
}

public String getEmail() {
    return email;
}
public void setEmail(String email) {
    this.email = email;
}

public String getPassword() {
    return password;
}
public void setPassword(String password) {
    this.password = password;
}

public Role getRole() {
    return role;
}
public void setRole(Role role) {
    this.role = role;
}

public boolean isBanned() {
    return banned;
}
public void setBanned(boolean banned) {
    this.banned = banned;
}

public boolean isChatRestricted() {
    return chatRestricted;
}
public void setChatRestricted(boolean chatRestricted) {
    this.chatRestricted = chatRestricted;
}

public List<Comment> getComments() {
    return comments;
}
public void setComments(List<Comment> comments) {
    this.comments = comments;
}

public List<Rating> getRatings() {
    return ratings;
}
public void setRatings(List<Rating> ratings) {
    this.ratings = ratings;
}

public List<Subscription> getSubscriptions() {
    return subscriptions;
}
public void setSubscriptions(List<Subscription> subscriptions) {
    this.subscriptions = subscriptions;
}
public List<Photo> getUploadedPhotos() {
    return uploadedPhotos;
}
public void setUploadedPhotos(List<Photo> uploadedPhotos) {
    this.uploadedPhotos = uploadedPhotos;
}
public void addComment(Comment comment) {
    comments.add(comment);
    comment.setUser(this);
}
public void removeComment(Comment comment) {
    comments.remove(comment);
    comment.setUser(null);
}
public void addRating(Rating rating) {
    ratings.add(rating);
    rating.setUser(this);   
}
public void removeRating(Rating rating) {
    ratings.remove(rating);
    rating.setUser(null);
}
public void addSubscription(Subscription subscription) {
    subscriptions.add(subscription);
    subscription.setUser(this);
}
public void removeSubscription(Subscription subscription) {
    subscriptions.remove(subscription);
    subscription.setUser(null);
}
public void addUploadedPhoto(Photo photo) {
    uploadedPhotos.add(photo);
    photo.setUploadedBy(this);
}
public void removeUploadedPhoto(Photo photo) {
    uploadedPhotos.remove(photo);
    photo.setUploadedBy(null);
}
public boolean isAdmin() {
    return this.role == Role.ADMIN;
}
public boolean isModerator() {
    return this.role == Role.MODERATOR;
}
}

