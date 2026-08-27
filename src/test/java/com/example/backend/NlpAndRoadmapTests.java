package com.example.backend;

import com.example.backend.model.*;
import com.example.backend.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NlpAndRoadmapTests {

    @Autowired
    private NlpEngineService nlpEngineService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RoadmapService roadmapService;

    @Autowired
    private StateService stateService;

    @BeforeEach
    void setUp() {
        // Reset states for clean tests
        stateService.setProfile(null);
        stateService.setRoadmap(new ArrayList<>());
        stateService.setChatMessages(new ArrayList<>());
    }

    @Test
    void testNlpParserSOCAnalyst() {
        String input = "I want to become a SOC Analyst. I know basic Python and Linux but I have never studied networking.";
        LearnerProfile profile = nlpEngineService.parseOnboardingInput(input);

        assertNotNull(profile);
        assertEquals("Cybersecurity SOC Analyst", profile.getTargetGoal());
        assertEquals("Intermediate", profile.getCurrentLevel());

        // Check skills
        List<SkillNode> skills = profile.getSkills();
        assertNotNull(skills);
        
        SkillNode networking = findSkill(skills, "Networking");
        assertNotNull(networking);
        assertTrue(networking.getLevel() <= 20, "Networking level should be low since they 'never studied networking'.");

        SkillNode python = findSkill(skills, "Python");
        assertNotNull(python);
        assertTrue(python.getLevel() >= 60, "Python level should be intermediate or higher.");

        SkillNode linux = findSkill(skills, "Linux");
        assertNotNull(linux);
        assertTrue(linux.getLevel() >= 60, "Linux level should be intermediate or higher.");
    }

    @Test
    void testAdaptiveRoadmapAssessmentFail() {
        // Onboard user
        String input = "I want to become a SOC Analyst. I know basic Python and Linux but I have never studied networking.";
        profileService.onboardUser(input);
        
        LearnerProfile profile = profileService.getProfile();
        roadmapService.initializeRoadmapForProfile(profile);

        // Fetch active module (should be Networking Fundamentals)
        RoadmapModule module = roadmapService.getModule("soc_mod1");
        assertNotNull(module);
        assertEquals("Available", module.getStatus());

        // Simulate failing the assessment
        // Q1 correct option is 2 (TCP). Let's submit wrong answers: [ "0", "0", "Too short" ]
        List<String> failedAnswers = Arrays.asList("0", "0", "No answer");
        String resultMsg = roadmapService.submitAssessment("soc_mod1", failedAnswers);

        assertTrue(resultMsg.contains("Assessment score"));
        assertEquals("Needs_Revision", module.getAssessmentStatus());
        assertEquals("Available", module.getStatus()); // still available for retry

        // Verify that remediation module was injected
        List<RoadmapPhase> roadmap = roadmapService.getRoadmap();
        boolean hasRemediation = false;
        for (RoadmapPhase phase : roadmap) {
            for (RoadmapModule mod : phase.getModules()) {
                if (mod.getId().equals("soc_mod1_remediation")) {
                    hasRemediation = true;
                    assertEquals("Available", mod.getStatus());
                    assertTrue(mod.getWhyRecommended().contains("Remediation"));
                }
            }
        }
        assertTrue(hasRemediation, "A remediation module should be injected in the roadmap on assessment failure.");
    }

    @Test
    void testAdaptiveRoadmapAssessmentPassAndFeedback() {
        // Onboard user
        String input = "I want to become a SOC Analyst. I know basic Python and Linux but I have never studied networking.";
        profileService.onboardUser(input);
        
        LearnerProfile profile = profileService.getProfile();
        roadmapService.initializeRoadmapForProfile(profile);

        // Simulate passing the assessment
        // Q1 correct is 2 (TCP), Q2 correct is 1 (Resolve domain names). Scenario answer > 8 chars.
        List<String> passingAnswers = Arrays.asList("2", "1", "This is a detailed scenario answer mapping SSH port 22.");
        String resultMsg = roadmapService.submitAssessment("soc_mod1", passingAnswers);

        assertTrue(resultMsg.contains("Congratulations"));
        RoadmapModule module = roadmapService.getModule("soc_mod1");
        assertEquals("Completed", module.getStatus());
        assertEquals("Strong_Understanding", module.getAssessmentStatus());

        // Verify next module is unlocked
        RoadmapModule nextModule = roadmapService.getModule("soc_mod2");
        assertNotNull(nextModule);
        assertEquals("Available", nextModule.getStatus(), "Next module should be unlocked and set to Available.");

        // Verify skill was updated in profile
        SkillNode networkingSkill = findSkill(profileService.getProfile().getSkills(), "Networking");
        assertNotNull(networkingSkill);
        assertTrue(networkingSkill.getLevel() >= 70, "Skill level should be updated after passing.");

        // Simulate submitting "Too Easy" feedback
        String feedbackMsg = roadmapService.submitFeedback("soc_mod1", "Too Easy", "It was quite straightforward.");
        assertTrue(feedbackMsg.contains("accelerated"), "Feedback response should state that path was accelerated.");

        // Verify next module whyRecommended was updated
        assertEquals("Accelerated: The previous topic was too easy for you, so we fast-tracked you to advanced exercises.", nextModule.getWhyRecommended());
    }

    private SkillNode findSkill(List<SkillNode> skills, String name) {
        return skills.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
