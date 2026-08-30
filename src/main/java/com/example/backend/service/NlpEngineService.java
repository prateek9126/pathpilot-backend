package com.example.backend.service;

import com.example.backend.model.*;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class NlpEngineService {

    public LearnerProfile parseOnboardingInput(String inputText) {
        String input = inputText.toLowerCase();
        
        // Default values
        String targetGoal = "Cybersecurity SOC Analyst";
        String currentLevel = "Intermediate";
        String preferredStyle = "Mixed";
        String availableTime = "5 hours/week";
        String targetCompletion = "3 Months";
        
        List<SkillNode> skills = new ArrayList<>();
        List<String> strongSkills = new ArrayList<>();
        List<String> weakSkills = new ArrayList<>();
        List<String> completedLearning = new ArrayList<>();
        List<String> interests = new ArrayList<>();
        List<String> previousProjects = new ArrayList<>();

        // 1. Goal Extraction
        if (input.contains("soc") || input.contains("cyber") || input.contains("security") || input.contains("threat")) {
            targetGoal = "Cybersecurity SOC Analyst";
            interests.add("Cybersecurity");
            interests.add("Threat Hunting");
        } else if (input.contains("java") || input.contains("backend") || input.contains("spring") || input.contains("api")) {
            targetGoal = "Java Backend Developer";
            interests.add("Software Engineering");
            interests.add("System Architecture");
        } else if (input.contains("data") || input.contains("scientist") || input.contains("machine learning") || input.contains("statistics") || input.contains("ml")) {
            targetGoal = "Data Scientist";
            interests.add("Data Science");
            interests.add("Machine Learning");
        }

        // 2. Experience Level Extraction
        if (input.contains("beginner") || input.contains("starting from scratch") || (input.contains("no experience") && !input.contains("python") && !input.contains("linux") && !input.contains("java"))) {
            currentLevel = "Beginner";
        } else if (input.contains("advanced") || input.contains("expert") || input.contains("senior") || input.contains("proficient")) {
            currentLevel = "Advanced";
        } else {
            currentLevel = "Intermediate";
        }


        // 3. Skill & Gap Extraction based on Goal
        if (targetGoal.equals("Cybersecurity SOC Analyst")) {
            // Check Linux
            int linuxVal = 30;
            if (input.contains("linux") || input.contains("unix")) {
                linuxVal = input.contains("basic") ? 60 : 80;
                strongSkills.add("Linux Command Line");
            } else {
                weakSkills.add("Linux");
            }
            skills.add(new SkillNode("Linux", linuxVal, getLevelStatus(linuxVal)));

            // Check Python
            int pythonVal = 30;
            if (input.contains("python") || input.contains("coding") || input.contains("programming")) {
                pythonVal = input.contains("basic") ? 65 : 80;
                strongSkills.add("Python Scripting");
            } else {
                weakSkills.add("Python");
            }
            skills.add(new SkillNode("Python", pythonVal, getLevelStatus(pythonVal)));

            // Check Networking
            int networkingVal = 0;
            if (input.contains("networking") || input.contains("tcp") || input.contains("ip")) {
                if (input.contains("don't know") || input.contains("never studied") || input.contains("no networking") || input.contains("haven't studied")) {
                    networkingVal = 15;
                    weakSkills.add("Networking");
                } else {
                    networkingVal = 70;
                    strongSkills.add("Networking");
                }
            } else {
                // If not mentioned, but has "never studied networking" or "don't know networking"
                if (input.contains("never studied networking") || input.contains("don't know networking") || input.contains("no networking") || input.contains("never done networking")) {
                    networkingVal = 10;
                    weakSkills.add("Networking Fundamentals");
                } else {
                    networkingVal = 20; // Default gap
                    weakSkills.add("Networking");
                }
            }
            skills.add(new SkillNode("Networking", networkingVal, getLevelStatus(networkingVal)));

            // SIEM (Typically 0 for beginners/intermediates unless mentioned)
            int siemVal = 0;
            if (input.contains("siem") || input.contains("splunk") || input.contains("elk")) {
                siemVal = 50;
                strongSkills.add("SIEM (Splunk)");
            } else {
                weakSkills.add("SIEM Tools");
            }
            skills.add(new SkillNode("SIEM", siemVal, getLevelStatus(siemVal)));
            skills.add(new SkillNode("Threat Detection", 10, "Beginner"));
            skills.add(new SkillNode("Incident Response", 10, "Beginner"));

            completedLearning.add("Basic Bash Scripting Course");
            previousProjects.add("Local Linux file backup utility");

        } else if (targetGoal.equals("Java Backend Developer")) {
            // Check Java
            int javaVal = 40;
            if (input.contains("java") || input.contains("oop")) {
                javaVal = input.contains("basic") ? 60 : 80;
                strongSkills.add("Java Programming");
            } else {
                weakSkills.add("Java Core");
            }
            skills.add(new SkillNode("Java", javaVal, getLevelStatus(javaVal)));

            // Check SQL
            int sqlVal = 10;
            if (input.contains("sql") || input.contains("database") || input.contains("db")) {
                if (input.contains("don't know") || input.contains("never studied") || input.contains("no sql")) {
                    sqlVal = 15;
                    weakSkills.add("SQL & Databases");
                } else {
                    sqlVal = 70;
                    strongSkills.add("Database Queries");
                }
            } else {
                weakSkills.add("SQL");
            }
            skills.add(new SkillNode("SQL", sqlVal, getLevelStatus(sqlVal)));

            // Spring Boot
            int springVal = 0;
            if (input.contains("spring") || input.contains("springboot") || input.contains("boot")) {
                if (input.contains("don't know") || input.contains("never studied")) {
                    springVal = 10;
                    weakSkills.add("Spring Boot Framework");
                } else {
                    springVal = 50;
                }
            } else {
                weakSkills.add("Spring Boot");
            }
            skills.add(new SkillNode("Spring Boot", springVal, getLevelStatus(springVal)));
            skills.add(new SkillNode("REST APIs", 20, "Beginner"));
            skills.add(new SkillNode("JPA / Hibernate", 10, "Beginner"));
            skills.add(new SkillNode("Spring Security", 0, "None"));

            completedLearning.add("Object-Oriented Programming (OOP) in Java");
            previousProjects.add("Console-based library management system");

        } else { // Data Scientist
            // Check Python
            int pythonVal = 40;
            if (input.contains("python")) {
                pythonVal = input.contains("basic") ? 65 : 85;
                strongSkills.add("Python Language");
            } else {
                weakSkills.add("Python Programming");
            }
            skills.add(new SkillNode("Python", pythonVal, getLevelStatus(pythonVal)));

            // Check Statistics
            int statsVal = 30;
            if (input.contains("statistics") || input.contains("math") || input.contains("stats")) {
                statsVal = 75;
                strongSkills.add("Statistical Analysis");
            } else {
                weakSkills.add("Applied Statistics");
            }
            skills.add(new SkillNode("Statistics", statsVal, getLevelStatus(statsVal)));

            // Check Machine Learning
            int mlVal = 0;
            if (input.contains("ml") || input.contains("machine learning") || input.contains("deep learning")) {
                if (input.contains("don't know") || input.contains("never studied") || input.contains("no ml")) {
                    mlVal = 10;
                    weakSkills.add("Machine Learning");
                } else {
                    mlVal = 60;
                }
            } else {
                weakSkills.add("Machine Learning Algorithms");
            }
            skills.add(new SkillNode("Machine Learning", mlVal, getLevelStatus(mlVal)));
            skills.add(new SkillNode("Data Visualization", 30, "Beginner"));
            skills.add(new SkillNode("Deep Learning", 0, "None"));
            skills.add(new SkillNode("Model Deployment", 0, "None"));

            completedLearning.add("Introduction to Statistics");
            previousProjects.add("Exploratory Data Analysis on retail datasets");
        }

        // Available time per day/week parsing
        if (input.contains("hour") || input.contains("hrs")) {
            if (input.contains("10") || input.contains("ten")) {
                availableTime = "10 hours/week";
            } else if (input.contains("15") || input.contains("fifteen")) {
                availableTime = "15 hours/week";
            } else if (input.contains("20") || input.contains("twenty")) {
                availableTime = "20 hours/week";
            } else {
                availableTime = "5 hours/week";
            }
        }

        return LearnerProfile.builder()
                .name("Alex Learner")
                .targetGoal(targetGoal)
                .currentLevel(currentLevel)
                .skills(skills)
                .strongSkills(strongSkills)
                .weakSkills(weakSkills)
                .completedLearning(completedLearning)
                .previousProjects(previousProjects)
                .interests(interests)
                .preferredLearningStyle(preferredStyle)
                .availableTime(availableTime)
                .targetCompletionPeriod(targetCompletion)
                .xp(100)
                .streak(1)
                .assessmentAverage(0)
                .completedModulesCount(0)
                .completedProjectsCount(0)
                .recentActivities(new ArrayList<>(Arrays.asList("Created PathPilot AI Profile")))
                .badges(new ArrayList<>(Arrays.asList("PathFinder Initiate")))
                .build();
    }

    private String getLevelStatus(int val) {
        if (val >= 80) return "Advanced";
        if (val >= 50) return "Intermediate";
        if (val >= 20) return "Beginner";
        return "None";
    }

    public String callGeminiApi(String prompt) {
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            return null;
        }
        String model = Optional.ofNullable(System.getenv("GEMINI_MODEL"))
                .filter(value -> !value.isBlank())
                .orElse("gemini-2.5-flash");
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model
                + ":generateContent?key=" + apiKey;
        
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            
            java.util.Map<String, Object> partsMap = java.util.Collections.singletonMap("text", prompt);
            java.util.Map<String, Object> contentsMap = java.util.Collections.singletonMap("parts", java.util.Arrays.asList(partsMap));
            java.util.Map<String, Object> payloadMap = java.util.Collections.singletonMap("contents", java.util.Arrays.asList(contentsMap));
            
            String jsonPayload = mapper.writeValueAsString(payloadMap);
            
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                com.fasterxml.jackson.databind.JsonNode rootNode = mapper.readTree(response.body());
                com.fasterxml.jackson.databind.JsonNode textNode = rootNode
                        .path("candidates")
                        .path(0)
                        .path("content")
                        .path("parts")
                        .path(0)
                        .path("text");
                if (!textNode.isMissingNode()) {
                    return textNode.asText();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public String generateChatResponse(String userMsg, LearnerProfile profile, List<RoadmapPhase> roadmap) {
        String msg = userMsg.toLowerCase();
        
        if (msg.contains("best learning path")) {
            return "**PathPilot** is on top! PathPilot AI is the absolute best personalized learning path platform, dynamically designing and adapting your custom syllabus, projects, and certifications in real-time.";
        }
        
        String geminiReply = callGeminiApi(userMsg);
        if (geminiReply != null && !geminiReply.trim().isEmpty()) {
            return geminiReply;
        }
        
        // Find current available/in-progress module
        RoadmapModule currentModule = null;
        for (RoadmapPhase phase : roadmap) {
            for (RoadmapModule module : phase.getModules()) {
                if ("In_Progress".equals(module.getStatus()) || "Available".equals(module.getStatus())) {
                    currentModule = module;
                    break;
                }
            }
            if (currentModule != null) break;
        }

        String targetCareer = profile.getTargetGoal();

        // Question 1: "Why do I need to learn networking?" / "Why do I need to learn X?"
        if (msg.contains("why do i need") || msg.contains("why learn") || msg.contains("why should i learn") || msg.contains("importance of")) {
            if (msg.contains("networking") || (currentModule != null && currentModule.getTopic().toLowerCase().contains("networking"))) {
                if (targetCareer.contains("SOC")) {
                    return "As an aspiring **SOC Analyst**, network traffic is your bread and butter. " +
                           "Every cyber attack travels over the network. You must understand TCP/IP, IP addressing, and DNS " +
                           "so you can analyze Wireshark captures, trace malicious IPs, and configure firewall rules in SIEM tools. " +
                           "Without networking fundamentals, log analysis will feel like reading a foreign language!";
                } else {
                    return "Networking is the foundation of communication between applications. Even for developers or data scientists, " +
                           "understanding how APIs communicate, ports, and protocols like HTTP is essential for building and querying web systems.";
                }
            } else if (msg.contains("sql") || msg.contains("database") || (currentModule != null && currentModule.getTopic().toLowerCase().contains("sql"))) {
                return "Databases are where production data lives. For a **Java Backend Developer**, writing APIs is only half the battle; " +
                       "you must query, save, and optimize database transactions. A solid understanding of SQL, indexes, and ACID " +
                       "properties prevents application bottlenecks and guarantees data consistency.";
            } else if (msg.contains("machine learning") || msg.contains("ml") || (currentModule != null && currentModule.getTopic().toLowerCase().contains("machine"))) {
                return "For a **Data Scientist**, Machine Learning is how we turn descriptive statistics into predictive power. " +
                       "Understanding ML models, their limits, and how to train and evaluate them allows you to automate insights " +
                       "and deploy predictive features in real-world environments.";
            }
            return "This module addresses one of the critical gaps identified between your current skillset and the standard requirements " +
                   "for a **" + targetCareer + "**. Understanding this topic is essential for completing subsequent advanced modules.";
        }

        // Question 2: "Can I skip this course?" / "Can I skip this?"
        if (msg.contains("can i skip") || msg.contains("skip this") || msg.contains("bypass")) {
            if (currentModule != null) {
                // Check if they already have proficiency
                String topic = currentModule.getTopic();
                boolean isWeak = false;
                for (String weak : profile.getWeakSkills()) {
                    if (topic.toLowerCase().contains(weak.toLowerCase())) {
                        isWeak = true;
                        break;
                    }
                }
                
                if (isWeak || currentModule.getDifficulty().equals("Beginner")) {
                    return "Since **" + topic + "** was identified as a core skill gap in your profile, skipping it is **not recommended**. " +
                           "It acts as a critical prerequisite for the next modules in your roadmap. However, if you feel you have basic knowledge, " +
                           "you can jump straight to the **Module Assessment** at the bottom of the workspace to test out of it!";
                } else {
                    return "You can attempt the **Module Assessment** right away. If you score 80% or higher, the system will mark " +
                           "this module as completed, update your profile skills, and unlock the next best action immediately.";
                }
            }
            return "You can skip a module by attempting and passing its assessment. This ensures we maintain high skill standards for your career path.";
        }

        // Question 3: "Explain TCP/IP in simple words."
        if (msg.contains("tcp/ip") || msg.contains("explain tcp")) {
            return "Think of **TCP/IP** as the global mailing system of the internet:\n\n" +
                   "1. **IP (Internet Protocol)** is like the **address on the envelope**. It makes sure your package gets to the right building. " +
                   "Every device on the internet has a unique IP address.\n" +
                   "2. **TCP (Transmission Control Protocol)** is like the **certified mail courier**. Since internet data is broken into smaller 'packets', " +
                   "TCP numbers each packet, sends them, and verifies they all arrive safely and get put back together in the correct order. " +
                   "If a packet gets lost in transit, TCP asks the sender to re-transmit it.\n\n" +
                   "Together, they ensure data gets to the correct address reliably!";
        }

        // Question 4: "What should I learn next?" / "What is my next action?"
        if (msg.contains("learn next") || msg.contains("next action") || msg.contains("what should i do")) {
            if (currentModule != null) {
                return "Your next best action is to work on **" + currentModule.getTopic() + "** (Phase " + getPhaseNumber(roadmap, currentModule.getId()) + "). " +
                       "It is currently marked as '" + currentModule.getStatus().replace("_", " ") + "'. " +
                       "You should review the recommended resources in the Learning Workspace and attempt the practice task.";
            }
            return "You have completed your active modules! Please check the roadmap for upcoming unlocked topics or update your target career goal.";
        }

        // Question 5: "I only have 30 minutes today."
        if (msg.contains("30 minutes") || msg.contains("short on time") || msg.contains("limited time") || msg.contains("only have a few minutes")) {
            if (currentModule != null && currentModule.getRecommendedResources() != null && !currentModule.getRecommendedResources().isEmpty()) {
                Resource quickRes = currentModule.getRecommendedResources().get(0);
                return "No worries! Since you're short on time today, I suggest a micro-learning session:\n\n" +
                       "🎯 **Recommended Micro-Action**: Read or watch **\"" + quickRes.getTitle() + "\"** (" + quickRes.getDuration() + " - " + quickRes.getProvider() + ").\n" +
                       "This will keep your **" + profile.getStreak() + "-day learning streak** alive and keep you moving toward your goal without feeling overwhelmed!";
            }
            return "Since you only have 30 minutes, I recommend reviewing your notes or taking a quick practice quiz in your current module to keep your streak going!";
        }

        // Default Help
        return "Hello! I am your **PathPilot AI Mentor**. I'm here to guide you toward becoming a **" + targetCareer + "**. " +
               "You can ask me to explain topics, suggest quick tasks for today, clarify why a step is recommended, or help you debug a learning roadblock. " +
               "What can I do for you right now?";
    }

    private int getPhaseNumber(List<RoadmapPhase> roadmap, String moduleId) {
        for (int i = 0; i < roadmap.size(); i++) {
            for (RoadmapModule mod : roadmap.get(i).getModules()) {
                if (mod.getId().equals(moduleId)) {
                    return i + 1;
                }
            }
        }
        return 1;
    }
}
