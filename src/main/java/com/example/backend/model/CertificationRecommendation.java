package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationRecommendation {
    private String id;
    private String name;
    private String provider;
    private String description;
    private String category; // Cybersecurity, Software Development, Cloud, Data Science, etc.
    private String difficulty; // Beginner, Intermediate, Advanced
    private int price; // in INR (₹0 means free)
    private boolean isFree;
    private String duration; // e.g. "30 hours" or "3 months"
    private String validity; // e.g. "3 Years" or "Lifetime"
    private String examFormat; // e.g. "Multiple Choice Questions (MCQ)"
    
    // Skill splits
    @Builder.Default
    private List<String> requiredSkills = new ArrayList<>();
    @Builder.Default
    private List<String> existingSkills = new ArrayList<>();
    @Builder.Default
    private List<String> skillsToLearn = new ArrayList<>();
    
    // Value parameters
    private String careerRelevance; // Low, Medium, High, Very High
    private String industryRecognition; // Low, Medium, High, Very High
    private String portfolioValue; // Low, Medium, High, Very High
    private int matchScore; // e.g. 96
    private String whyRecommended;
    
    // Checklists & status
    @Builder.Default
    private List<String> benefits = new ArrayList<>();
    @Builder.Default
    private List<String> preparationRoadmap = new ArrayList<>();
    @Builder.Default
    private List<Boolean> completedPhases = new ArrayList<>(Arrays.asList(false, false, false, false, false));
    
    @Builder.Default
    private String status = "INTERESTED"; // INTERESTED, SAVED, PREPARING, EXAM_SCHEDULED, COMPLETED, EXPIRED
    private int progress; // 0 to 100%
    private boolean saved;
}
