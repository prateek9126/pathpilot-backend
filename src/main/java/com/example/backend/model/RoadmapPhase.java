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
public class RoadmapPhase {
    private String id;
    private String title;
    private String description;
    private String status; // Locked, In_Progress, Completed
    private List<RoadmapModule> modules;
}
