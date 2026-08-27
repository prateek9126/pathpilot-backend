package com.example.backend.service;

import com.example.backend.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StateService {
    private LearnerProfile profile;
    private List<RoadmapPhase> roadmap = new ArrayList<>();
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public LearnerProfile getProfile() {
        return profile;
    }

    public void setProfile(LearnerProfile profile) {
        this.profile = profile;
    }

    public List<RoadmapPhase> getRoadmap() {
        return roadmap;
    }

    public void setRoadmap(List<RoadmapPhase> roadmap) {
        this.roadmap = roadmap;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void addChatMessage(ChatMessage message) {
        this.chatMessages.add(message);
    }
}
