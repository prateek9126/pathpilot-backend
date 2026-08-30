package com.example.backend.controller;

import com.example.backend.model.*;
import com.example.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping({"/api", ""})
@CrossOrigin(
    origins = {"https://pathpilot-frontend-06df.onrender.com", "http://localhost:5174", "http://localhost:3000"},
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowedHeaders = "*")
public class ApiController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RoadmapService roadmapService;

    @Autowired
    private NlpEngineService nlpEngineService;

    @Autowired
    private StateService stateService;

    // 1. Get current profile
    @GetMapping("/profile")
    public ResponseEntity<LearnerProfile> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    // 2. Onboard user (Natural Language or Form)
    @PostMapping("/onboard")
    public ResponseEntity<LearnerProfile> onboard(@RequestBody Map<String, Object> request) {
        LearnerProfile profile;
        if (request.containsKey("naturalLanguage") && request.get("naturalLanguage") != null) {
            String prompt = (String) request.get("naturalLanguage");
            profile = profileService.onboardUser(prompt);
        } else {
            // Map the JSON structure to LearnerProfile (simplified)
            // Expects form mapping
            Map<String, Object> profileData = (Map<String, Object>) request.get("profile");
            LearnerProfile form = convertMapToProfile(profileData);
            profile = profileService.onboardUserForm(form);
        }
        
        // Build initial roadmap for the profile
        roadmapService.initializeRoadmapForProfile(profile);
        
        return ResponseEntity.ok(profile);
    }

    // 3. Update profile manually
    @PutMapping("/profile")
    public ResponseEntity<LearnerProfile> updateProfile(@RequestBody LearnerProfile profile) {
        LearnerProfile updated = profileService.updateProfile(profile);
        // Adapt roadmap based on manual changes
        roadmapService.initializeRoadmapForProfile(updated);
        return ResponseEntity.ok(updated);
    }

    // 4. Get active roadmap
    @GetMapping("/roadmap")
    public ResponseEntity<List<RoadmapPhase>> getRoadmap() {
        return ResponseEntity.ok(roadmapService.getRoadmap());
    }

    // 5. Get specific module details
    @GetMapping("/roadmap/modules/{moduleId}")
    public ResponseEntity<RoadmapModule> getModule(@PathVariable String moduleId) {
        RoadmapModule module = roadmapService.getModule(moduleId);
        if (module != null) {
            return ResponseEntity.ok(module);
        }
        return ResponseEntity.notFound().build();
    }

    // 6. Mark module completed (without assessment)
    @PostMapping("/roadmap/modules/{moduleId}/complete")
    public ResponseEntity<Map<String, Object>> completeModule(@PathVariable String moduleId) {
        roadmapService.markModuleCompleted(moduleId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Module completed successfully.");
        response.put("profile", profileService.getProfile());
        response.put("roadmap", roadmapService.getRoadmap());
        return ResponseEntity.ok(response);
    }

    // 7. Submit module assessment
    @PostMapping("/roadmap/modules/{moduleId}/assessment")
    public ResponseEntity<Map<String, Object>> submitAssessment(
            @PathVariable String moduleId, 
            @RequestBody Map<String, List<String>> request) {
        
        List<String> answers = request.get("answers");
        String evaluationMessage = roadmapService.submitAssessment(moduleId, answers);
        RoadmapModule module = roadmapService.getModule(moduleId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("score", module != null ? module.getScore() : 0);
        response.put("status", module != null ? module.getAssessmentStatus() : "Needs_Revision");
        response.put("message", evaluationMessage);
        response.put("profile", profileService.getProfile());
        response.put("roadmap", roadmapService.getRoadmap());
        return ResponseEntity.ok(response);
    }

    // 8. Submit module feedback
    @PostMapping("/roadmap/modules/{moduleId}/feedback")
    public ResponseEntity<Map<String, Object>> submitFeedback(
            @PathVariable String moduleId,
            @RequestBody Map<String, String> request) {
        
        String difficulty = request.get("difficulty");
        String struggles = request.get("struggles");
        
        String adaptationMessage = roadmapService.submitFeedback(moduleId, difficulty, struggles);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", adaptationMessage);
        response.put("profile", profileService.getProfile());
        response.put("roadmap", roadmapService.getRoadmap());
        return ResponseEntity.ok(response);
    }

    // 9. Load a specific demo profile
    @PostMapping("/demo/{demoId}")
    public ResponseEntity<Map<String, Object>> loadDemo(@PathVariable String demoId) {
        profileService.loadDemo(demoId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("profile", profileService.getProfile());
        response.put("roadmap", roadmapService.getRoadmap());
        return ResponseEntity.ok(response);
    }

    // 10. Get chat history
    @GetMapping("/ai/chat")
    public ResponseEntity<List<ChatMessage>> getChatMessages() {
        return ResponseEntity.ok(stateService.getChatMessages());
    }

    // 11. Send a chat message to the AI Mentor
    @PostMapping("/ai/chat")
    public ResponseEntity<ChatMessage> sendChatMessage(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        if (text == null || text.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        
        // 1. Add user message to history
        ChatMessage userMsg = ChatMessage.builder()
                .sender("user")
                .text(text)
                .timestamp(System.currentTimeMillis())
                .build();
        stateService.addChatMessage(userMsg);
        
        // 2. Generate and add AI response
        String aiResponse = nlpEngineService.generateChatResponse(
                text, 
                profileService.getProfile(), 
                roadmapService.getRoadmap()
        );
        
        ChatMessage assistantMsg = ChatMessage.builder()
                .sender("assistant")
                .text(aiResponse)
                .timestamp(System.currentTimeMillis())
                .build();
        stateService.addChatMessage(assistantMsg);
        
        return ResponseEntity.ok(assistantMsg);
    }

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/recommendations")
    public ResponseEntity<List<LearningResource>> getRecommendations(
            @RequestParam(defaultValue = "1000000") int maxBudget,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String platform) {
        
        LearnerProfile profile = profileService.getProfile();
        List<RoadmapPhase> roadmap = roadmapService.getRoadmap();
        
        List<LearningResource> list = recommendationService.getRecommendations(
                profile, roadmap, maxBudget, type, search, sortBy, skill, difficulty, platform
        );
        return ResponseEntity.ok(list);
    }

    @Autowired
    private ProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectRecommendation>> getProjects() {
        LearnerProfile profile = profileService.getProfile();
        return ResponseEntity.ok(projectService.getRecommendations(profile));
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectRecommendation> getProjectDetails(@PathVariable String projectId) {
        LearnerProfile profile = profileService.getProfile();
        ProjectRecommendation p = projectService.getProject(projectId, profile);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PostMapping("/projects/{projectId}/start")
    public ResponseEntity<ProjectRecommendation> startProject(@PathVariable String projectId) {
        LearnerProfile profile = profileService.getProfile();
        ProjectRecommendation p = projectService.startProject(projectId, profile);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PostMapping("/projects/{projectId}/milestones/{phaseIndex}/complete")
    public ResponseEntity<ProjectRecommendation> completeMilestone(
            @PathVariable String projectId,
            @PathVariable int phaseIndex) {
        LearnerProfile profile = profileService.getProfile();
        ProjectRecommendation p = projectService.toggleMilestone(projectId, phaseIndex, profile);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PostMapping("/projects/{projectId}/status")
    public ResponseEntity<ProjectRecommendation> updateProjectStatus(
            @PathVariable String projectId,
            @RequestBody Map<String, String> body) {
        String status = body.getOrDefault("status", "IN_PROGRESS");
        LearnerProfile profile = profileService.getProfile();
        ProjectRecommendation p = projectService.updateStatus(projectId, status, profile);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PostMapping("/projects/{projectId}/assistant")
    public ResponseEntity<Map<String, String>> projectAssistantChat(
            @PathVariable String projectId,
            @RequestBody Map<String, String> body) {
        String query = body.getOrDefault("query", "");
        LearnerProfile profile = profileService.getProfile();
        String reply = projectService.generateAssistantResponse(projectId, query, profile);
        return ResponseEntity.ok(Map.of("reply", reply));
    }

    @PostMapping("/projects/compare")
    public ResponseEntity<List<ProjectRecommendation>> compareProjects(@RequestBody List<String> projectIds) {
        LearnerProfile profile = profileService.getProfile();
        List<ProjectRecommendation> list = projectIds.stream()
                .map(id -> projectService.getProject(id, profile))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Autowired
    private CertificationService certificationService;

    @GetMapping("/certifications")
    public ResponseEntity<List<CertificationRecommendation>> getCertifications(
            @RequestParam(defaultValue = "1000000") int maxBudget,
            @RequestParam(required = false) String category) {
        LearnerProfile profile = profileService.getProfile();
        return ResponseEntity.ok(certificationService.getRecommendations(profile, maxBudget, category));
    }

    @GetMapping("/certifications/{certId}")
    public ResponseEntity<CertificationRecommendation> getCertDetails(@PathVariable String certId) {
        LearnerProfile profile = profileService.getProfile();
        CertificationRecommendation c = certificationService.getCertDetails(certId, profile);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(c);
    }

    @PostMapping("/certifications/{certId}/save")
    public ResponseEntity<CertificationRecommendation> saveCert(@PathVariable String certId) {
        return ResponseEntity.ok(certificationService.saveCertification(certId, true));
    }

    @PostMapping("/certifications/{certId}/unsave")
    public ResponseEntity<CertificationRecommendation> unsaveCert(@PathVariable String certId) {
        return ResponseEntity.ok(certificationService.saveCertification(certId, false));
    }

    @PostMapping("/certifications/{certId}/status")
    public ResponseEntity<CertificationRecommendation> updateCertStatus(
            @PathVariable String certId,
            @RequestBody Map<String, String> body) {
        String status = body.getOrDefault("status", "PREPARING");
        return ResponseEntity.ok(certificationService.updateStatus(certId, status));
    }

    @PostMapping("/certifications/{certId}/milestones/{phaseIndex}/complete")
    public ResponseEntity<CertificationRecommendation> completeCertMilestone(
            @PathVariable String certId,
            @PathVariable int phaseIndex) {
        return ResponseEntity.ok(certificationService.toggleMilestone(certId, phaseIndex));
    }

    @PostMapping("/certifications/{certId}/assistant")
    public ResponseEntity<Map<String, String>> certAssistantChat(
            @PathVariable String certId,
            @RequestBody Map<String, String> body) {
        String query = body.getOrDefault("query", "");
        String reply = certificationService.generateAssistantResponse(certId, query);
        return ResponseEntity.ok(Map.of("reply", reply));
    }

    @PostMapping("/certifications/compare")
    public ResponseEntity<List<CertificationRecommendation>> compareCertifications(@RequestBody List<String> certIds) {
        LearnerProfile profile = profileService.getProfile();
        List<CertificationRecommendation> list = certIds.stream()
                .map(id -> certificationService.getCertDetails(id, profile))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Autowired
    private CareerService careerService;

    @GetMapping("/career/overview")
    public ResponseEntity<Map<String, Object>> getCareerOverview() {
        LearnerProfile profile = profileService.getProfile();
        return ResponseEntity.ok(careerService.getOverview(profile));
    }

    @GetMapping("/career/transition")
    public ResponseEntity<Map<String, Object>> getTransitionRoadmap(@RequestParam String target) {
        LearnerProfile profile = profileService.getProfile();
        return ResponseEntity.ok(careerService.getTransitionPlan(target, profile));
    }

    @GetMapping("/career/companies")
    public ResponseEntity<List<Map<String, String>>> getCompanyJobs(@RequestParam String target) {
        return ResponseEntity.ok(careerService.getCompanyOpenings(target));
    }

    @GetMapping("/career/skill")
    public ResponseEntity<Map<String, Object>> searchSkillDemandDetails(@RequestParam String skill) {
        return ResponseEntity.ok(careerService.searchSkillDemand(skill));
    }

    @PostMapping("/career/advisor")
    public ResponseEntity<Map<String, String>> careerAdvisorChat(@RequestBody Map<String, String> body) {
        String query = body.getOrDefault("query", "");
        LearnerProfile profile = profileService.getProfile();
        String reply = careerService.generateAdvisorResponse(query, profile);
        return ResponseEntity.ok(Map.of("reply", reply));
    }

    // Convert raw Map to LearnerProfile
    private LearnerProfile convertMapToProfile(Map<String, Object> data) {
        if (data == null) return new LearnerProfile();
        
        List<SkillNode> skills = new ArrayList<>();
        if (data.containsKey("skills")) {
            List<Map<String, Object>> skillsList = (List<Map<String, Object>>) data.get("skills");
            for (Map<String, Object> sMap : skillsList) {
                skills.add(SkillNode.builder()
                        .name((String) sMap.get("name"))
                        .level((Integer) sMap.get("level"))
                        .status((String) sMap.get("status"))
                        .build());
            }
        }
        
        List<String> completed = data.containsKey("completedLearning") ? (List<String>) data.get("completedLearning") : new ArrayList<>();
        List<String> projects = data.containsKey("previousProjects") ? (List<String>) data.get("previousProjects") : new ArrayList<>();
        List<String> interests = data.containsKey("interests") ? (List<String>) data.get("interests") : new ArrayList<>();

        return LearnerProfile.builder()
                .name((String) data.getOrDefault("name", "Alex Learner"))
                .targetGoal((String) data.getOrDefault("targetGoal", "Cybersecurity SOC Analyst"))
                .currentLevel((String) data.getOrDefault("currentLevel", "Intermediate"))
                .preferredLearningStyle((String) data.getOrDefault("preferredLearningStyle", "Mixed"))
                .availableTime((String) data.getOrDefault("availableTime", "5 hours/week"))
                .targetCompletionPeriod((String) data.getOrDefault("targetCompletionPeriod", "3 Months"))
                .skills(skills)
                .completedLearning(completed)
                .previousProjects(projects)
                .interests(interests)
                .build();
    }
}
