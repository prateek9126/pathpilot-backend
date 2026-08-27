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
public class CareerPath {
    private String name;
    private String description;
    private String difficulty; // Low, Medium, High, Very High
    private String codingRequired; // Low, Medium, High, Very High
    private String mathRequired; // Low, Medium, High, Very High
    private String marketDemand; // Low, Medium, High, Very High
    private int beginnerFriendliness; // 1 to 5 stars
    
    @Builder.Default
    private List<String> typicalProjects = new ArrayList<>();
    @Builder.Default
    private List<String> certifications = new ArrayList<>();
    private String careerProgression; // e.g. "Junior -> Mid -> Senior -> Lead"
    
    private int matchScore; // computed dynamically
    private String learningTime; // e.g. "4-6 months"
}
