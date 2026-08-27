package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {
    private String id;
    private String title;
    private String type; // Course, Video, Article, Exercise, Documentation
    private String url;
    private String provider;
    private String duration;
    private String difficulty;
}
