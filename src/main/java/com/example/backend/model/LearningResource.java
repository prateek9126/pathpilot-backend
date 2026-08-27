package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningResource {
    private String id;
    private String title;
    private String platform;
    private String type; // YouTube Videos, YouTube Playlists, Online Courses, Interactive Coding Courses, Documentation, Practice/Assessment, Certification Courses
    private String url;
    private String thumbnail;
    private String instructor;
    private String skill;
    private String difficulty; // Beginner, Intermediate, Advanced
    private String duration;
    private double rating;
    private int price; // in INR
    private String currency; // "₹"
    private boolean isFree;
    private String description;
    
    // Dynamic ranking fields
    private int relevanceScore;
    private String whyRecommended;
}
