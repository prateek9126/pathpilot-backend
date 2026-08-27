package com.example.backend.service;

import com.example.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class RoadmapService {

    @Autowired
    private StateService stateService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private DemoService demoService;

    public List<RoadmapPhase> getRoadmap() {
        if (stateService.getRoadmap() == null || stateService.getRoadmap().isEmpty()) {
            // Load default SOC roadmap if empty
            profileService.loadDemo("soc");
        }
        return stateService.getRoadmap();
    }

    public void initializeRoadmapForProfile(LearnerProfile profile) {
        String goal = profile.getTargetGoal().toLowerCase();
        if (goal.contains("soc") || goal.contains("security")) {
            stateService.setRoadmap(demoService.getDemoRoadmap("soc"));
        } else if (goal.contains("java") || goal.contains("backend")) {
            stateService.setRoadmap(demoService.getDemoRoadmap("java"));
        } else if (goal.contains("data") || goal.contains("scientist") || goal.contains("machine")) {
            stateService.setRoadmap(demoService.getDemoRoadmap("ds"));
        } else {
            // Fallback generic web developer roadmap
            stateService.setRoadmap(generateFallbackRoadmap(profile.getTargetGoal()));
        }
        
        // Mark first module as Available if all are locked
        unlockFirstModule();
    }

    private void unlockFirstModule() {
        List<RoadmapPhase> roadmap = stateService.getRoadmap();
        if (roadmap != null && !roadmap.isEmpty()) {
            RoadmapPhase firstPhase = roadmap.get(0);
            firstPhase.setStatus("In_Progress");
            if (firstPhase.getModules() != null && !firstPhase.getModules().isEmpty()) {
                firstPhase.getModules().get(0).setStatus("Available");
            }
        }
    }

    public RoadmapModule getModule(String moduleId) {
        for (RoadmapPhase phase : getRoadmap()) {
            for (RoadmapModule module : phase.getModules()) {
                if (module.getId().equals(moduleId)) {
                    return module;
                }
            }
        }
        return null;
    }

    public void markModuleCompleted(String moduleId) {
        RoadmapModule module = getModule(moduleId);
        if (module != null && !"Completed".equals(module.getStatus())) {
            module.setStatus("Completed");
            profileService.awardXp(50);
            profileService.logActivity("Completed module: " + module.getTopic());
            unlockNextModule(moduleId);
            updateProfileProgress();
        }
    }

    public String submitAssessment(String moduleId, List<String> userAnswers) {
        RoadmapModule module = getModule(moduleId);
        if (module == null) return "Module not found";

        List<RoadmapModule.Question> questions = module.getAssessmentQuestions();
        if (questions == null || questions.isEmpty()) {
            // Auto-pass if no questions
            module.setStatus("Completed");
            module.setScore(100);
            module.setAssessmentStatus("Strong_Understanding");
            unlockNextModule(moduleId);
            updateProfileProgress();
            return "Assessment passed! (No questions, automatically approved)";
        }

        int correctCount = 0;
        int totalQuestions = questions.size();

        for (int i = 0; i < totalQuestions; i++) {
            RoadmapModule.Question q = questions.get(i);
            String userAns = (userAnswers != null && i < userAnswers.size()) ? userAnswers.get(i) : "";
            
            if ("MCQ".equalsIgnoreCase(q.getType())) {
                try {
                    int selectedIndex = Integer.parseInt(userAns);
                    if (selectedIndex == q.getCorrectOptionIndex()) {
                        correctCount++;
                    }
                } catch (NumberFormatException e) {
                    // Fail gracefully
                }
            } else {
                // Scenario or ShortAnswer: evaluate length & relevance (simplified for demo)
                if (userAns.trim().length() > 8) {
                    correctCount++;
                }
            }
        }

        int score = (int) Math.round(((double) correctCount / totalQuestions) * 100);
        module.setScore(score);

        LearnerProfile profile = profileService.getProfile();
        String skillName = extractSkillFromModule(module.getTopic());

        if (score >= 70) {
            String status = score >= 85 ? "Strong_Understanding" : "Passed";
            module.setAssessmentStatus(status);
            module.setStatus("Completed");
            
            // Award XP
            int xpAwarded = score >= 85 ? 100 : 75;
            profileService.awardXp(xpAwarded);
            profileService.awardBadge("First Assessment Passed");
            
            if ("soc_mod1".equals(moduleId)) {
                profileService.awardBadge("Networking Basics");
            }
            
            // Update profile skill
            updateProfileSkill(skillName, score);
            profileService.logActivity("Passed assessment for " + module.getTopic() + " — Score: " + score + "%");
            
            unlockNextModule(moduleId);
            updateProfileProgress();
            
            return "Congratulations! You passed the assessment with a score of " + score + "% (" + status.replace("_", " ") + ").";
        } else {
            module.setAssessmentStatus("Needs_Revision");
            profileService.awardXp(15); // small consolation XP
            profileService.logActivity("Failed assessment for " + module.getTopic() + " — Score: " + score + "%");
            
            // Adapt roadmap: inject practice sub-module
            injectPracticeModule(module);
            updateProfileProgress();
            
            return "Assessment score: " + score + "%. You need at least 70% to pass. We've updated your roadmap and injected a specialized practice module to help you review before retaking the assessment.";
        }
    }

    public String submitFeedback(String moduleId, String difficulty, String struggles) {
        RoadmapModule module = getModule(moduleId);
        if (module == null) return "Module not found";

        module.setFeedbackDifficulty(difficulty);
        module.setFeedbackStruggles(struggles);
        profileService.logActivity("Submitted feedback for: " + module.getTopic() + " (" + difficulty + ")");
        
        // Find next module to adapt
        RoadmapModule nextModule = getNextModule(moduleId);
        
        if (nextModule == null) {
            return "Thank you for your feedback! You've reached the end of the roadmap.";
        }

        if ("Too Easy".equalsIgnoreCase(difficulty) || "Easy".equalsIgnoreCase(difficulty)) {
            // Speed up path: skip intro level resources or increase proficiency
            nextModule.setWhyRecommended("Accelerated: The previous topic was too easy for you, so we fast-tracked you to advanced exercises.");
            nextModule.setEstimatedDuration("Reduced! (Estimated time shortened by 20%)");
            
            // Give extra skill bump in profile
            String skill = extractSkillFromModule(module.getTopic());
            increaseSkillByValue(skill, 10);
            
            return "Based on your feedback that the module was easy, we have accelerated your roadmap! Your skill rating for " + skill + " has been boosted, and the next module's duration has been optimized.";
        } else if ("Difficult".equalsIgnoreCase(difficulty) || "Very Difficult".equalsIgnoreCase(difficulty)) {
            // Add extra deep dive resources to next module
            List<Resource> resources = new ArrayList<>(nextModule.getRecommendedResources());
            Resource extraRes = Resource.builder()
                    .id("extra_" + nextModule.getId())
                    .title("Deep Dive Review: " + module.getTopic() + " Remediation Guide")
                    .type("Article")
                    .provider("PathPilot AI Mentor")
                    .duration("15 minutes")
                    .url("#")
                    .difficulty("Beginner")
                    .build();
            resources.add(0, extraRes);
            nextModule.setRecommendedResources(resources);
            nextModule.setWhyRecommended("Adapted: Extra fundamentals and deep-dive materials were added because you struggled with " + module.getTopic() + ".");
            
            return "We heard you! Since you found " + module.getTopic() + " challenging, we've updated your roadmap. An extra deep-dive guide has been added to your upcoming modules to ensure a smooth transition.";
        }

        return "Feedback recorded. Your roadmap remains optimized for a 'Just Right' learning pace.";
    }

    private void updateProfileSkill(String skillName, int score) {
        LearnerProfile profile = profileService.getProfile();
        if (profile != null && profile.getSkills() != null) {
            for (SkillNode skill : profile.getSkills()) {
                if (skill.getName().equalsIgnoreCase(skillName)) {
                    int previous = skill.getLevel();
                    int updatedLevel = Math.max(previous, score - 5); // set proficiency slightly below test score or keep previous if higher
                    skill.setLevel(updatedLevel);
                    skill.setStatus(updatedLevel >= 80 ? "Advanced" : (updatedLevel >= 50 ? "Intermediate" : "Beginner"));
                    break;
                }
            }
        }
    }

    private void increaseSkillByValue(String skillName, int value) {
        LearnerProfile profile = profileService.getProfile();
        if (profile != null && profile.getSkills() != null) {
            for (SkillNode skill : profile.getSkills()) {
                if (skill.getName().equalsIgnoreCase(skillName)) {
                    int updatedLevel = Math.min(100, skill.getLevel() + value);
                    skill.setLevel(updatedLevel);
                    skill.setStatus(updatedLevel >= 80 ? "Advanced" : (updatedLevel >= 50 ? "Intermediate" : "Beginner"));
                    break;
                }
            }
        }
    }

    private void injectPracticeModule(RoadmapModule parentModule) {
        List<RoadmapPhase> roadmap = stateService.getRoadmap();
        String targetId = parentModule.getId() + "_remediation";

        // Check if already injected
        for (RoadmapPhase phase : roadmap) {
            for (RoadmapModule module : phase.getModules()) {
                if (module.getId().equals(targetId)) {
                    // reset status to available
                    module.setStatus("Available");
                    return;
                }
            }
        }

        // Create remediation module
        RoadmapModule remediation = RoadmapModule.builder()
                .id(targetId)
                .topic(parentModule.getTopic() + " Remediation")
                .description("A review module containing supplementary materials and basic exercises to help you master " + parentModule.getTopic() + " after scoring " + parentModule.getScore() + "% on the assessment.")
                .estimatedDuration("20 minutes")
                .difficulty("Beginner")
                .prerequisites(Collections.emptyList())
                .whyRecommended("Remediation: Injected because your score of " + parentModule.getScore() + "% in " + parentModule.getTopic() + " was below the 70% passing threshold.")
                .status("Available")
                .objectives(Arrays.asList("Review core principles of " + parentModule.getTopic(), "Practice foundational quiz questions", "Solve interactive exercise"))
                .practiceTask("Read the remediation guide and complete the mini-quiz.")
                .recommendedResources(Arrays.asList(
                        Resource.builder().id("rem_res1").title(parentModule.getTopic() + " Remediation Cheat Sheet").type("Article").provider("PathPilot AI").duration("10m").url("#").difficulty("Beginner").build(),
                        Resource.builder().id("rem_res2").title("Interactive Sandbox: " + parentModule.getTopic() + " Review").type("Exercise").provider("PathPilot").duration("15m").url("#").difficulty("Beginner").build()
                ))
                .assessmentQuestions(Arrays.asList(
                        RoadmapModule.Question.builder().id("rem_q1").type("MCQ").questionText("Which of the following is most essential to review when struggling with " + parentModule.getTopic() + "?")
                                .options(Arrays.asList("Skipping directly to the next phase", "Reviewing foundational terminology", "Failing and giving up")).correctOptionIndex(1).build()
                ))
                .build();

        // Inject in the same phase as parentModule
        for (RoadmapPhase phase : roadmap) {
            int parentIdx = -1;
            for (int i = 0; i < phase.getModules().size(); i++) {
                if (phase.getModules().get(i).getId().equals(parentModule.getId())) {
                    parentIdx = i;
                    break;
                }
            }
            if (parentIdx != -1) {
                phase.getModules().add(parentIdx + 1, remediation);
                break;
            }
        }

        // Set parent module to available so they can retake it later
        parentModule.setStatus("Available");
    }

    private void unlockNextModule(String currentModuleId) {
        List<RoadmapPhase> roadmap = stateService.getRoadmap();
        boolean foundCurrent = false;

        for (int p = 0; p < roadmap.size(); p++) {
            RoadmapPhase phase = roadmap.get(p);
            for (int m = 0; m < phase.getModules().size(); m++) {
                RoadmapModule module = phase.getModules().get(m);
                if (module.getId().equals(currentModuleId)) {
                    foundCurrent = true;
                    // check if there's a next module in the SAME phase
                    if (m + 1 < phase.getModules().size()) {
                        RoadmapModule next = phase.getModules().get(m + 1);
                        if (!"Completed".equals(next.getStatus())) {
                            next.setStatus("Available");
                        }
                        return;
                    }
                    // check if there's a next PHASE
                    if (p + 1 < roadmap.size()) {
                        RoadmapPhase nextPhase = roadmap.get(p + 1);
                        nextPhase.setStatus("In_Progress");
                        if (nextPhase.getModules() != null && !nextPhase.getModules().isEmpty()) {
                            RoadmapModule next = nextPhase.getModules().get(0);
                            if (!"Completed".equals(next.getStatus())) {
                                next.setStatus("Available");
                            }
                        }
                        // Mark current phase completed if all its modules are completed
                        checkAndCompletePhase(phase);
                        return;
                    }
                }
            }
        }
    }

    private void checkAndCompletePhase(RoadmapPhase phase) {
        boolean allComplete = true;
        for (RoadmapModule mod : phase.getModules()) {
            if (!"Completed".equals(mod.getStatus())) {
                allComplete = false;
                break;
            }
        }
        if (allComplete) {
            phase.setStatus("Completed");
        }
    }

    private RoadmapModule getNextModule(String currentModuleId) {
        List<RoadmapPhase> roadmap = stateService.getRoadmap();
        boolean foundCurrent = false;

        for (int p = 0; p < roadmap.size(); p++) {
            RoadmapPhase phase = roadmap.get(p);
            for (int m = 0; m < phase.getModules().size(); m++) {
                RoadmapModule module = phase.getModules().get(m);
                if (foundCurrent) {
                    return module;
                }
                if (module.getId().equals(currentModuleId)) {
                    foundCurrent = true;
                    if (m + 1 < phase.getModules().size()) {
                        return phase.getModules().get(m + 1);
                    }
                    if (p + 1 < roadmap.size()) {
                        RoadmapPhase nextPhase = roadmap.get(p + 1);
                        if (nextPhase.getModules() != null && !nextPhase.getModules().isEmpty()) {
                            return nextPhase.getModules().get(0);
                        }
                    }
                }
            }
        }
        return null;
    }

    private void updateProfileProgress() {
        LearnerProfile profile = profileService.getProfile();
        if (profile == null) return;

        int totalModules = 0;
        int completedModules = 0;
        int scoreSum = 0;
        int gradedCount = 0;

        for (RoadmapPhase phase : getRoadmap()) {
            for (RoadmapModule module : phase.getModules()) {
                totalModules++;
                if ("Completed".equals(module.getStatus())) {
                    completedModules++;
                }
                if (module.getScore() != null) {
                    scoreSum += module.getScore();
                    gradedCount++;
                }
            }
        }

        profile.setCompletedModulesCount(completedModules);
        if (gradedCount > 0) {
            profile.setAssessmentAverage(scoreSum / gradedCount);
        }
        
        // Award Milestones Badges
        if (completedModules >= 1) {
            profileService.awardBadge("First Module Completed");
        }
        if (completedModules == totalModules && totalModules > 0) {
            profileService.awardBadge("Roadmap Completed");
        }
    }

    private String extractSkillFromModule(String topic) {
        String t = topic.toLowerCase();
        if (t.contains("network")) return "Networking";
        if (t.contains("linux") || t.contains("syslog")) return "Linux";
        if (t.contains("python")) return "Python";
        if (t.contains("siem") || t.contains("splunk")) return "SIEM";
        if (t.contains("log")) return "Log Analysis";
        if (t.contains("mitre")) return "MITRE ATT&CK";
        if (t.contains("threat")) return "Threat Detection";
        if (t.contains("incident")) return "Incident Response";
        if (t.contains("sql") || t.contains("database")) return "SQL";
        if (t.contains("spring core")) return "Spring Boot";
        if (t.contains("spring boot") || t.contains("rest api")) return "Spring Boot";
        if (t.contains("jpa") || t.contains("hibernate")) return "JPA / Hibernate";
        if (t.contains("security")) return "Spring Security";
        if (t.contains("pandas") || t.contains("numpy") || t.contains("visualization")) return "Data Visualization";
        if (t.contains("machine learning") || t.contains("ml")) return "Machine Learning";
        return topic;
    }

    private List<RoadmapPhase> generateFallbackRoadmap(String targetGoal) {
        List<RoadmapPhase> phases = new ArrayList<>();
        
        RoadmapModule module = RoadmapModule.builder()
                .id("gen_mod1")
                .topic("Introduction to " + targetGoal)
                .description("Explore core concepts and requirements for building a career as a " + targetGoal + ".")
                .estimatedDuration("30 minutes")
                .difficulty("Beginner")
                .prerequisites(Collections.emptyList())
                .whyRecommended("Kickstart your roadmap for " + targetGoal)
                .status("Available")
                .objectives(Arrays.asList("Identify key industry skills", "Analyze average career milestones", "Set up local workspace"))
                .practiceTask("Draft a list of target technologies you plan to master.")
                .recommendedResources(Arrays.asList(
                        Resource.builder().id("gen_res1").title("Introduction to " + targetGoal).type("Article").provider("PathPilot").duration("10m").url("#").difficulty("Beginner").build()
                ))
                .assessmentQuestions(Arrays.asList(
                        RoadmapModule.Question.builder().id("gen_q1").type("MCQ").questionText("What is the primary key skill needed for " + targetGoal + "?")
                                .options(Arrays.asList("Consistent practice", "Failing to plan", "Copying code without thinking")).correctOptionIndex(0).build()
                ))
                .build();

        phases.add(RoadmapPhase.builder()
                .id("gen_phase1")
                .title("Phase 1 — Introduction")
                .description("Overview and tools preparation.")
                .status("In_Progress")
                .modules(new ArrayList<>(Arrays.asList(module)))
                .build());

        return phases;
    }
}
