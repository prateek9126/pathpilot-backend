package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillNode {
    private String name;
    private int level; // 0 to 100
    private String status; // e.g. "Beginner", "Intermediate", "Advanced"
}
