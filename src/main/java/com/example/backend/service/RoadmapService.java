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
                    ensureTenQuestions(module);
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

    private void ensureTenQuestions(RoadmapModule module) {
        if (module == null) return;
        List<RoadmapModule.Question> questions = module.getAssessmentQuestions();
        if (questions == null) {
            questions = new ArrayList<>();
            module.setAssessmentQuestions(questions);
        } else {
            questions = new ArrayList<>(questions);
            module.setAssessmentQuestions(questions);
        }

        if (questions.size() >= 10) {
            if (questions.size() > 10) {
                module.setAssessmentQuestions(new ArrayList<>(questions.subList(0, 10)));
            }
            return;
        }

        int needed = 10 - questions.size();
        List<RoadmapModule.Question> generated = generateMockQuestionsForTopic(module.getId(), module.getTopic(), needed, questions.size());
        questions.addAll(generated);
    }

    private List<RoadmapModule.Question> generateMockQuestionsForTopic(String moduleId, String topic, int count, int startIndex) {
        List<RoadmapModule.Question> list = new ArrayList<>();
        String t = topic.toLowerCase();
        List<RoadmapModule.Question> pool = new ArrayList<>();
        
        if (t.contains("network") || t.contains("tcp") || t.contains("port") || t.contains("ip")) {
            pool.add(createMCQ("q_net_a1", "Which OSI layer is responsible for packet routing?", Arrays.asList("Data Link Layer", "Network Layer", "Transport Layer", "Physical Layer"), 1));
            pool.add(createMCQ("q_net_a2", "What port number is standard for HTTPS?", Arrays.asList("80", "22", "443", "8080"), 2));
            pool.add(createMCQ("q_net_a3", "Which protocol provides connectionless, low-overhead transmission?", Arrays.asList("TCP", "UDP", "FTP", "SSH"), 1));
            pool.add(createMCQ("q_net_a4", "What subnet mask corresponds to a /24 CIDR block?", Arrays.asList("255.255.0.0", "255.255.255.0", "255.0.0.0", "255.255.255.255"), 1));
            pool.add(createMCQ("q_net_a5", "Which device operates primarily at the Data Link Layer (Layer 2)?", Arrays.asList("Router", "Switch", "Hub", "Repeater"), 1));
            pool.add(createMCQ("q_net_a6", "What is the primary function of DHCP?", Arrays.asList("Domain resolution", "IP address allocation", "Routing packets", "Traffic encryption"), 1));
            pool.add(createMCQ("q_net_a7", "Which command is used to test connectivity between hosts?", Arrays.asList("ping", "netstat", "ipconfig", "nslookup"), 0));
            pool.add(createMCQ("q_net_a8", "Which protocol resolves a MAC address from a known IP address?", Arrays.asList("DNS", "ARP", "DHCP", "ICMP"), 1));
            pool.add(createMCQ("q_net_a9", "What is the loopback IPv4 address?", Arrays.asList("192.168.1.1", "10.0.0.1", "127.0.0.1", "0.0.0.0"), 2));
            pool.add(createMCQ("q_net_a10", "Which protocol is used to securely copy files over a network?", Arrays.asList("FTP", "SCP/SFTP", "Telnet", "HTTP"), 1));
        } else if (t.contains("sql") || t.contains("database") || t.contains("queries")) {
            pool.add(createMCQ("q_sql_a1", "Which SQL command is used to retrieve data from a database?", Arrays.asList("SELECT", "GET", "EXTRACT", "QUERY"), 0));
            pool.add(createMCQ("q_sql_a2", "What is a primary key constraint in SQL?", Arrays.asList("Enforces unique values and no nulls", "Enforces index sorting only", "Enforces database speed", "Enforces foreign key relationships"), 0));
            pool.add(createMCQ("q_sql_a3", "Which SQL join returns all records when there is a match in either table?", Arrays.asList("INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL OUTER JOIN"), 3));
            pool.add(createMCQ("q_sql_a4", "How do you filter database results in SQL?", Arrays.asList("WHERE clause", "HAVING clause only", "GROUP BY", "ORDER BY"), 0));
            pool.add(createMCQ("q_sql_a5", "Which SQL function is used to count the number of rows?", Arrays.asList("SUM()", "COUNT()", "NUMBER()", "TOTAL()"), 1));
            pool.add(createMCQ("q_sql_a6", "What SQL command is used to add new rows to a table?", Arrays.asList("INSERT INTO", "ADD ROW", "UPDATE", "CREATE"), 0));
            pool.add(createMCQ("q_sql_a7", "Which statement is used to remove duplicate values from a SELECT query?", Arrays.asList("UNIQUE", "DISTINCT", "DIFFERENT", "GROUP"), 1));
            pool.add(createMCQ("q_sql_a8", "How do you sort query results in descending order?", Arrays.asList("SORT DESC", "ORDER BY DESC", "ORDER BY LIMIT", "GROUP BY DESC"), 1));
            pool.add(createMCQ("q_sql_a9", "Which SQL wildcard matches zero or more characters?", Arrays.asList("_", "%", "*", "?"), 1));
            pool.add(createMCQ("q_sql_a10", "What is a foreign key?", Arrays.asList("A primary key of another table", "An encrypted key", "A backup key", "A local index key"), 0));
        } else if (t.contains("java") && !t.contains("javascript")) {
            pool.add(createMCQ("q_java_a1", "Which data type is used to store decimal values in Java?", Arrays.asList("int", "float/double", "char", "boolean"), 1));
            pool.add(createMCQ("q_java_a2", "What is the entry point method for any Java application?", Arrays.asList("public void main()", "public static void main(String[] args)", "void start()", "init()"), 1));
            pool.add(createMCQ("q_java_a3", "Which keyword is used to inherit a class in Java?", Arrays.asList("implements", "extends", "inherits", "using"), 1));
            pool.add(createMCQ("q_java_a4", "What is garbage collection in Java?", Arrays.asList("Deletes source files", "Cleans unused memory", "Reports errors", "Compiles code"), 1));
            pool.add(createMCQ("q_java_a5", "Which package is imported by default in every Java program?", Arrays.asList("java.util", "java.lang", "java.io", "java.net"), 1));
            pool.add(createMCQ("q_java_a6", "Which keyword makes a variable immutable in Java?", Arrays.asList("const", "static", "final", "abstract"), 2));
            pool.add(createMCQ("q_java_a7", "What is method overloading in Java?", Arrays.asList("Methods with same name but different parameters", "Overwriting parent method", "Calling methods too fast", "Methods returning multiple values"), 0));
            pool.add(createMCQ("q_java_a8", "Which class is the superclass of all classes in Java?", Arrays.asList("Class", "Object", "System", "String"), 1));
            pool.add(createMCQ("q_java_a9", "Which collection type does not allow duplicate elements?", Arrays.asList("ArrayList", "HashSet", "LinkedList", "HashMap"), 1));
            pool.add(createMCQ("q_java_a10", "What exception is thrown when accessing a null object reference?", Arrays.asList("NullPointerException", "ArrayIndexOutOfBoundsException", "ArithmeticException", "IOException"), 0));
        } else if (t.contains("linux") || t.contains("syslog") || t.contains("bash")) {
            pool.add(createMCQ("q_lin_a1", "Which Linux command displays the current directory path?", Arrays.asList("ls", "cd", "pwd", "dir"), 2));
            pool.add(createMCQ("q_lin_a2", "How do you change file permissions in Linux?", Arrays.asList("chown", "chmod", "chperm", "chgrp"), 1));
            pool.add(createMCQ("q_lin_a3", "Which command is used to search text patterns in files?", Arrays.asList("find", "grep", "locate", "search"), 1));
            pool.add(createMCQ("q_lin_a4", "What file stores local user account information in Linux?", Arrays.asList("/etc/passwd", "/etc/shadow", "/etc/hosts", "/etc/fstab"), 0));
            pool.add(createMCQ("q_lin_a5", "Which command displays currently running processes?", Arrays.asList("df", "free", "top", "uname"), 2));
            pool.add(createMCQ("q_lin_a6", "How do you kill a running process by its PID?", Arrays.asList("stop", "kill", "terminate", "del"), 1));
            pool.add(createMCQ("q_lin_a7", "Which folder holds system configuration files in Linux?", Arrays.asList("/bin", "/etc", "/var", "/tmp"), 1));
            pool.add(createMCQ("q_lin_a8", "Which command is used to read logs live in real-time?", Arrays.asList("cat", "nano", "tail -f", "less"), 2));
            pool.add(createMCQ("q_lin_a9", "What is the superuser account name in Linux?", Arrays.asList("admin", "root", "administrator", "sys"), 1));
            pool.add(createMCQ("q_lin_a10", "Which protocol is standard for secure shell access in Linux?", Arrays.asList("Telnet", "SSH", "FTP", "HTTP"), 1));
        } else {
            pool.add(createMCQ("q_gen_a1", "Which of the following represents a key step in mastering " + topic + "?", Arrays.asList("Consistent practice and coding", "Copying code blindly", "Skipping assessments", "Failing to read requirements"), 0));
            pool.add(createMCQ("q_gen_a2", "What does 'DRY' stand for in software development principles?", Arrays.asList("Do Repeat Yourself", "Don't Repeat Yourself", "Detailed Resource Yield", "Distributed Routing Yield"), 1));
            pool.add(createMCQ("q_gen_a3", "What is the primary role of version control systems like Git?", Arrays.asList("Compile code", "Track code revisions and collaborate", "Enforce security policies", "Host database servers"), 1));
            pool.add(createMCQ("q_gen_a4", "In software engineering, what is debugging?", Arrays.asList("Writing unit tests", "Finding and resolving code defects", "Creating API endpoints", "Compiling production packages"), 1));
            pool.add(createMCQ("q_gen_a5", "What is an API?", Arrays.asList("Application Programming Interface", "Advanced Program Integration", "Automated Process Instance", "Abstract Parameter Identifier"), 0));
            pool.add(createMCQ("q_gen_a6", "Which data structure operates on a Last In, First Out (LIFO) basis?", Arrays.asList("Queue", "Stack", "List", "Tree"), 1));
            pool.add(createMCQ("q_gen_a7", "Which methodology prioritizes iterative development and user feedback?", Arrays.asList("Waterfall", "Agile", "Linear", "Monolithic"), 1));
            pool.add(createMCQ("q_gen_a8", "What is refactoring?", Arrays.asList("Rewriting code to improve structure without changing behavior", "Compiling java classes", "Encrypting database credentials", "Adding new features"), 0));
            pool.add(createMCQ("q_gen_a9", "Which format is standard for REST API JSON payload transfers?", Arrays.asList("XML", "JSON", "YAML", "CSV"), 1));
            pool.add(createMCQ("q_gen_a10", "What does CI/CD stand for?", Arrays.asList("Continuous Integration / Continuous Deployment", "Code Inspection / Code Delivery", "Centralized Indexing / Cloud Database", "Compiler Instance / Configuration Driver"), 0));
        }

        int poolIndex = 0;
        while (list.size() < count) {
            RoadmapModule.Question q = pool.get(poolIndex % pool.size());
            String uniqueId = moduleId + "_" + q.getId() + "_" + (startIndex + list.size());
            list.add(createMCQ(uniqueId, q.getQuestionText(), q.getOptions(), q.getCorrectOptionIndex()));
            poolIndex++;
        }
        
        return list;
    }

    private RoadmapModule.Question createMCQ(String id, String text, List<String> options, int correctIdx) {
        return RoadmapModule.Question.builder()
                .id(id)
                .type("MCQ")
                .questionText(text)
                .options(options)
                .correctOptionIndex(correctIdx)
                .build();
    }
}
