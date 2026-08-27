package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearnerProfile {
    private String name;
    private String targetGoal;
    private String currentLevel; // Beginner, Intermediate, Advanced
    
    @Builder.Default
    private List<SkillNode> skills = new ArrayList<>();
    
    @Builder.Default
    private List<String> strongSkills = new ArrayList<>();
    
    @Builder.Default
    private List<String> weakSkills = new ArrayList<>();
    
    @Builder.Default
    private List<String> completedLearning = new ArrayList<>();
    
    @Builder.Default
    private List<String> previousProjects = new ArrayList<>();
    
    @Builder.Default
    private List<String> interests = new ArrayList<>();
    
    private String preferredLearningStyle; // Videos, Reading, Hands-on, Interactive, Mixed
    private String availableTime; // e.g. "5 hours/week"
    private String targetCompletionPeriod; // e.g. "3 Months"
    
    // Gamification & Progress metrics
    @Builder.Default
    private int xp = 0;
    
    @Builder.Default
    private int streak = 1;
    
    @Builder.Default
    private int completedModulesCount = 0;
    
    @Builder.Default
    private int completedProjectsCount = 0;
    
    @Builder.Default
    private int assessmentAverage = 0;
    
    @Builder.Default
    private List<String> recentActivities = new ArrayList<>();
    
    @Builder.Default
    private List<String> badges = new ArrayList<>(); // e.g., "Networking Basics", "First Assessment Passed", etc.
}

