package com.example.backend.service;

import com.example.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private StateService stateService;

    @Autowired
    private NlpEngineService nlpEngineService;

    @Autowired
    private DemoService demoService;

    public LearnerProfile getProfile() {
        if (stateService.getProfile() == null) {
            // Load default SOC demo if not initialized
            this.loadDemo("soc");
        }
        return stateService.getProfile();
    }

    public LearnerProfile onboardUser(String naturalLanguageInput) {
        LearnerProfile profile = nlpEngineService.parseOnboardingInput(naturalLanguageInput);
        stateService.setProfile(profile);
        stateService.getChatMessages().clear(); // Reset chat
        return profile;
    }

    public LearnerProfile onboardUserForm(LearnerProfile form) {
        // Compute initial skills lists
        List<String> strong = new ArrayList<>();
        List<String> weak = new ArrayList<>();
        if (form.getSkills() != null) {
            for (SkillNode s : form.getSkills()) {
                if (s.getLevel() >= 70) {
                    strong.add(s.getName() + " (" + s.getLevel() + "%)");
                } else if (s.getLevel() <= 30) {
                    weak.add(s.getName() + " (" + s.getLevel() + "%)");
                }
            }
        }
        form.setStrongSkills(strong);
        form.setWeakSkills(weak);
        form.setXp(100);
        form.setStreak(1);
        form.setRecentActivities(new ArrayList<>(List.of("Onboarded via form details")));
        form.setBadges(new ArrayList<>(List.of("PathFinder Initiate")));
        
        stateService.setProfile(form);
        stateService.getChatMessages().clear(); // Reset chat
        return form;
    }

    private boolean isSameGoalCategory(String g1, String g2) {
        if (g1 == null || g2 == null) return false;
        return getGoalCategory(g1).equals(getGoalCategory(g2));
    }

    private String getGoalCategory(String goal) {
        String g = goal.toLowerCase();
        if (g.contains("soc") || g.contains("cyber") || g.contains("security")) return "soc";
        if (g.contains("java") || g.contains("backend") || g.contains("spring")) return "java";
        if (g.contains("data") || g.contains("science") || g.contains("ml") || g.contains("stats") || g.contains("scientist")) return "ds";
        return "generic";
    }

    public List<SkillNode> getDefaultSkillsForGoal(String goal) {
        String cat = getGoalCategory(goal);
        if ("soc".equals(cat)) {
            return new ArrayList<>(Arrays.asList(
                new SkillNode("Linux", 30, "Beginner"),
                new SkillNode("Python", 30, "Beginner"),
                new SkillNode("Networking", 20, "Beginner"),
                new SkillNode("SIEM", 0, "None"),
                new SkillNode("Log Analysis", 10, "Beginner"),
                new SkillNode("MITRE ATT&CK", 0, "None"),
                new SkillNode("Threat Detection", 5, "Beginner"),
                new SkillNode("Incident Response", 0, "None")
            ));
        } else if ("java".equals(cat)) {
            return new ArrayList<>(Arrays.asList(
                new SkillNode("Java", 40, "Beginner"),
                new SkillNode("OOP", 40, "Beginner"),
                new SkillNode("SQL", 20, "Beginner"),
                new SkillNode("Spring Boot", 15, "Beginner"),
                new SkillNode("REST APIs", 10, "Beginner"),
                new SkillNode("JPA / Hibernate", 0, "None"),
                new SkillNode("Spring Security", 0, "None")
            ));
        } else if ("ds".equals(cat)) {
            return new ArrayList<>(Arrays.asList(
                new SkillNode("Python", 40, "Beginner"),
                new SkillNode("Statistics", 30, "Beginner"),
                new SkillNode("Machine Learning", 10, "Beginner"),
                new SkillNode("Data Visualization", 30, "Beginner"),
                new SkillNode("Deep Learning", 0, "None"),
                new SkillNode("Model Deployment", 0, "None")
            ));
        } else {
            return new ArrayList<>(Arrays.asList(
                new SkillNode("Core Concepts", 20, "Beginner")
            ));
        }
    }

    public LearnerProfile updateProfile(LearnerProfile updated) {
        LearnerProfile current = getProfile();
        String oldGoal = current.getTargetGoal();
        String newGoal = updated.getTargetGoal();
        
        current.setName(updated.getName());
        current.setTargetGoal(newGoal);
        current.setCurrentLevel(updated.getCurrentLevel());
        current.setPreferredLearningStyle(updated.getPreferredLearningStyle());
        current.setAvailableTime(updated.getAvailableTime());
        current.setTargetCompletionPeriod(updated.getTargetCompletionPeriod());
        
        if (!isSameGoalCategory(oldGoal, newGoal)) {
            List<SkillNode> defaultSkills = getDefaultSkillsForGoal(newGoal);
            current.setSkills(defaultSkills);
        } else if (updated.getSkills() != null && !updated.getSkills().isEmpty()) {
            current.setSkills(updated.getSkills());
        }
        
        // Recalculate strong and weak
        List<String> strong = new ArrayList<>();
        List<String> weak = new ArrayList<>();
        for (SkillNode s : current.getSkills()) {
            if (s.getLevel() >= 70) {
                strong.add(s.getName());
            } else if (s.getLevel() <= 30) {
                weak.add(s.getName());
            }
        }
        current.setStrongSkills(strong);
        current.setWeakSkills(weak);
        
        logActivity("Updated Learner Profile settings & recalculated path");
        return current;
    }


    public void awardXp(int amount) {
        LearnerProfile profile = getProfile();
        if (profile != null) {
            profile.setXp(profile.getXp() + amount);
        }
    }

    public void awardBadge(String badgeName) {
        LearnerProfile profile = getProfile();
        if (profile != null && !profile.getBadges().contains(badgeName)) {
            profile.getBadges().add(badgeName);
            profile.getRecentActivities().add(0, "🏆 Earned Badge: " + badgeName);
        }
    }

    public void logActivity(String activity) {
        LearnerProfile profile = getProfile();
        if (profile != null) {
            profile.getRecentActivities().add(0, activity);
            if (profile.getRecentActivities().size() > 15) {
                profile.getRecentActivities().remove(profile.getRecentActivities().size() - 1);
            }
        }
    }

    public void loadDemo(String demoId) {
        stateService.setProfile(demoService.getDemoProfile(demoId));
        stateService.setRoadmap(demoService.getDemoRoadmap(demoId));
        stateService.getChatMessages().clear();
    }
}
