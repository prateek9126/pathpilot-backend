package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapModule {
    private String id;
    private String topic;
    private String description;
    private String estimatedDuration;
    private String difficulty; // Beginner, Intermediate, Advanced
    private List<String> prerequisites;
    private List<Resource> recommendedResources;
    private String practiceTask;
    private List<String> objectives;
    private String status; // Locked, Available, In_Progress, Completed
    private Integer score; // null if not taken
    private String assessmentStatus; // Passed, Needs_Revision, Strong_Understanding, null
    private List<Question> assessmentQuestions;
    
    // Feedback
    private String feedbackDifficulty; // Too Easy, Easy, Just Right, Difficult, Very Difficult
    private String feedbackStruggles;
    
    // Recommendation context
    private String whyRecommended;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Question {
        private String id;
        private String type; // MCQ, Scenario, ShortAnswer
        private String questionText;
        private List<String> options; // for MCQ
        private Integer correctOptionIndex; // for MCQ validation
        private String correctAnswer; // for validation
    }
}
