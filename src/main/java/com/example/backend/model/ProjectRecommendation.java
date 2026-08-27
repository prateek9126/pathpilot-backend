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
public class ProjectRecommendation {
    private String id;
    private String name;
    private String description;
    private String category;
    private String difficulty; // Beginner, Intermediate, Advanced
    private String duration; // e.g. "1-2 weeks"
    private int matchScore; // e.g. 94
    private String portfolioValue; // "Medium", "High", "Very High"
    private String whyRecommended;
    
    // Skill Split
    @Builder.Default
    private List<String> requiredSkills = new ArrayList<>();
    @Builder.Default
    private List<String> existingSkills = new ArrayList<>();
    @Builder.Default
    private List<String> skillsToLearn = new ArrayList<>();
    @Builder.Default
    private List<String> skillsToImprove = new ArrayList<>();
    
    // Tech Stack Map
    @Builder.Default
    private Map<String, String> technologyStack = new HashMap<>();
    
    // Uniqueness & Benefits
    @Builder.Default
    private List<String> benefits = new ArrayList<>();
    private String basicVersion;
    private String advancedVersion;
    private String uniqueVersion;
    
    // Checklist Features
    @Builder.Default
    private List<String> mvpFeatures = new ArrayList<>();
    @Builder.Default
    private List<String> advancedFeatures = new ArrayList<>();
    @Builder.Default
    private List<String> aiFeatures = new ArrayList<>();
    @Builder.Default
    private List<String> uniqueFeatures = new ArrayList<>();
    
    // Roadmap & Tracking
    @Builder.Default
    private List<String> roadmap = new ArrayList<>();
    @Builder.Default
    private List<Boolean> completedPhases = new ArrayList<>(Arrays.asList(false, false, false, false, false, false));
    
    @Builder.Default
    private String status = "NOT_STARTED"; // NOT_STARTED, IN_PROGRESS, PAUSED, COMPLETED
    private int progress; // 0 to 100%
}
