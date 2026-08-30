package com.example.backend.service;

import com.example.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CareerService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RoadmapService roadmapService;

    @Autowired
    private NlpEngineService nlpEngineService;

    private final List<CareerPath> careersDb = new ArrayList<>();
    private final Map<String, List<Map<String, String>>> companyJobs = new HashMap<>();

    public CareerService() {
        seedCareers();
        seedCompanyJobs();
    }

    private void seedCareers() {
        careersDb.add(CareerPath.builder()
                .name("Frontend Development")
                .description("Build highly interactive, beautiful, and accessible web user interfaces using modern frameworks.")
                .difficulty("Medium")
                .codingRequired("High")
                .mathRequired("Low")
                .marketDemand("High")
                .beginnerFriendliness(5)
                .typicalProjects(Arrays.asList("E-Commerce Storefront", "SaaS Dashboard", "Interactive Network Graph"))
                .certifications(Arrays.asList("Meta Frontend Developer Professional Certificate", "Google UX Design Certificate"))
                .careerProgression("Junior UI Engineer -> Senior Frontend Developer -> Frontend Architect")
                .learningTime("3-4 months")
                .build());

        careersDb.add(CareerPath.builder()
                .name("Full-Stack Development")
                .description("Build end-to-end software applications, handling client user experiences, application programming controllers, and database layers.")
                .difficulty("Medium")
                .codingRequired("High")
                .mathRequired("Low")
                .marketDemand("Very High")
                .beginnerFriendliness(4)
                .typicalProjects(Arrays.asList("Online Collaborative Board", "Expensing Split API & Dashboard", "Secure Ledger Web App"))
                .certifications(Arrays.asList("AWS Certified Developer", "Oracle Java SE Programmer"))
                .careerProgression("Full-Stack Engineer -> Lead Backend Architect -> Engineering Manager")
                .learningTime("5-6 months")
                .build());

        careersDb.add(CareerPath.builder()
                .name("Data Science")
                .description("Extract actionable insights from raw data using statistical tests, data cleanup pipelines, and predictive algorithms.")
                .difficulty("High")
                .codingRequired("High")
                .mathRequired("High")
                .marketDemand("High")
                .beginnerFriendliness(3)
                .typicalProjects(Arrays.asList("Predictive Customer Churn Classifier", "Housing Price Estimator Model", "Sales Forecast System"))
                .certifications(Arrays.asList("Google Data Analytics Professional Certificate", "IBM Data Science Certificate"))
                .careerProgression("Junior Analyst -> Data Scientist -> Principal Research Lead")
                .learningTime("4-6 months")
                .build());

        careersDb.add(CareerPath.builder()
                .name("AI/ML Engineering")
                .description("Design, develop, and host machine learning models, convolutional neural networks, and generative AI interfaces at scale.")
                .difficulty("Very High")
                .codingRequired("Very High")
                .mathRequired("High")
                .marketDemand("Very High")
                .beginnerFriendliness(2)
                .typicalProjects(Arrays.asList("Real-time Sentiment Stream", "Visual Defect Sensor Tool", "LLM Fine-tuning Playbook"))
                .certifications(Arrays.asList("TensorFlow Developer Certificate", "AWS Machine Learning Specialty"))
                .careerProgression("ML Engineer -> AI Scientist -> Chief AI Architect")
                .learningTime("6-12 months")
                .build());

        careersDb.add(CareerPath.builder()
                .name("Cybersecurity SOC Analyst")
                .description("Monitor corporate networks for anomalies, investigate logs, write SIEM alerts, and mitigate incident security breaches.")
                .difficulty("Medium")
                .codingRequired("Medium")
                .mathRequired("Low")
                .marketDemand("High")
                .beginnerFriendliness(3)
                .typicalProjects(Arrays.asList("AI Threat Detection Dashboard", "Phishing URL Sandboxing Tool", "Feed Aggregator CLI"))
                .certifications(Arrays.asList("Google Cybersecurity Certificate", "CompTIA Security+"))
                .careerProgression("L1 SOC Analyst -> Incident Response Lead -> Chief Information Security Officer (CISO)")
                .learningTime("4-5 months")
                .build());

        careersDb.add(CareerPath.builder()
                .name("Cloud/DevOps")
                .description("Automate software release pipelines, configure cloud load balancers, orchestrate containers, and index monitoring telemetry.")
                .difficulty("High")
                .codingRequired("Medium")
                .mathRequired("Low")
                .marketDemand("Very High")
                .beginnerFriendliness(2)
                .typicalProjects(Arrays.asList("Kubernetes Deployment Playbook", "Docker CI/CD Pipeline", "Terraform AWS Cluster"))
                .certifications(Arrays.asList("AWS Solutions Architect - Associate", "Certified Kubernetes Administrator (CKA)"))
                .careerProgression("Site Reliability Engineer (SRE) -> DevOps Team Lead -> Cloud Architect")
                .learningTime("5-7 months")
                .build());
    }

    private void seedCompanyJobs() {
        companyJobs.put("Cybersecurity SOC Analyst", Arrays.asList(
                Map.of("company", "InfoSec Guard", "role", "L1 Security Analyst", "skills", "Log Analysis, SIEM, Linux", "location", "Bengaluru (Hybrid)", "link", "#"),
                Map.of("company", "SecureNet Technologies", "role", "Incident Responder", "skills", "Incident Response, Networking, Threat Detection", "location", "Remote", "link", "#"),
                Map.of("company", "Cyber Shield Corp", "role", "SOC Team Lead", "skills", "SIEM, MITRE ATT&CK, Threat Detection", "location", "Mumbai (On-site)", "link", "#")
        ));
        
        companyJobs.put("Java Backend Developer", Arrays.asList(
                Map.of("company", "CoreFin Tech", "role", "Junior Java Backend Developer", "skills", "Java, SQL, OOP", "location", "Bengaluru (On-site)", "link", "#"),
                Map.of("company", "PaySafe Solutions", "role", "Senior Spring Boot Engineer", "skills", "Java, Spring Boot, Spring Security, REST APIs", "location", "Remote", "link", "#"),
                Map.of("company", "CloudMarket Inc", "role", "Microservices Architect", "skills", "Java, Spring Boot, REST APIs, JPA / Hibernate", "location", "Hyderabad (Hybrid)", "link", "#")
        ));

        companyJobs.put("Data Scientist", Arrays.asList(
                Map.of("company", "DataMetrics Inc", "role", "Data Analyst", "skills", "Python, Statistics, Data Visualization", "location", "Bengaluru (Hybrid)", "link", "#"),
                Map.of("company", "ML Alpha Lab", "role", "Research Scientist", "skills", "Python, Statistics, Machine Learning", "location", "Remote", "link", "#"),
                Map.of("company", "RetailAI Group", "role", "Data Science Engineer", "skills", "Python, Machine Learning, Data Visualization", "location", "Pune (On-site)", "link", "#")
        ));
    }

    public Map<String, Object> getOverview(LearnerProfile profile) {
        // Calculate current career path details
        String goal = profile.getTargetGoal();
        String trackKey = getTrackKey(goal);
        
        // Dynamic Match Scores for alternative careers
        List<CareerPath> alternatives = new ArrayList<>();
        for (CareerPath raw : careersDb) {
            CareerPath path = CareerPath.builder()
                    .name(raw.getName())
                    .description(raw.getDescription())
                    .difficulty(raw.getDifficulty())
                    .codingRequired(raw.getCodingRequired())
                    .mathRequired(raw.getMathRequired())
                    .marketDemand(raw.getMarketDemand())
                    .beginnerFriendliness(raw.getBeginnerFriendliness())
                    .typicalProjects(raw.getTypicalProjects())
                    .certifications(raw.getCertifications())
                    .careerProgression(raw.getCareerProgression())
                    .learningTime(raw.getLearningTime())
                    .build();

            int match = calculateMatchScore(path.getName(), profile);
            path.setMatchScore(match);
            alternatives.add(path);
        }

        // Sort alternatives
        alternatives.sort((c1, c2) -> Integer.compare(c2.getMatchScore(), c1.getMatchScore()));

        // Stay or Switch Recommendation
        Map<String, Object> staySwitch = getStaySwitchAdvice(profile, trackKey);

        Map<String, Object> response = new HashMap<>();
        response.put("currentGoal", goal);
        response.put("currentLevel", profile.getCurrentLevel());
        response.put("profileSkills", profile.getSkills());
        response.put("alternatives", alternatives);
        response.put("staySwitch", staySwitch);
        response.put("lastUpdated", "August 2026");
        return response;
    }

    private String getTrackKey(String goal) {
        String g = goal.toLowerCase();
        if (g.contains("java") || g.contains("backend") || g.contains("spring")) return "Java Backend Developer";
        if (g.contains("data") || g.contains("scientist") || g.contains("machine") || g.contains("ds")) return "Data Scientist";
        return "Cybersecurity SOC Analyst";
    }

    private int calculateMatchScore(String careerName, LearnerProfile profile) {
        List<String> required = getRequiredSkillsForCareer(careerName);
        if (required.isEmpty()) return 50;

        int matchedCount = 0;
        for (String req : required) {
            boolean hasIt = profile.getSkills().stream()
                    .anyMatch(s -> s.getName().equalsIgnoreCase(req) && s.getLevel() >= 40);
            if (hasIt) matchedCount++;
        }

        int score = (matchedCount * 100) / required.size();
        return Math.max(50, score);
    }

    private List<String> getRequiredSkillsForCareer(String careerName) {
        switch (careerName) {
            case "Frontend Development":
                return Arrays.asList("JavaScript", "React", "Data Visualization");
            case "Full-Stack Development":
                return Arrays.asList("Java", "OOP", "SQL", "Spring Boot", "REST APIs");
            case "Data Science":
                return Arrays.asList("Python", "Statistics", "Machine Learning", "Data Visualization");
            case "AI/ML Engineering":
                return Arrays.asList("Python", "Statistics", "Machine Learning");
            case "Cybersecurity SOC Analyst":
                return Arrays.asList("Linux", "Python", "Networking", "SIEM", "Threat Detection");
            case "Cloud/DevOps":
                return Arrays.asList("Linux", "Networking", "Spring Boot");
            default:
                return new ArrayList<>();
        }
    }

    public Map<String, Object> getTransitionPlan(String targetCareer, LearnerProfile profile) {
        List<String> required = getRequiredSkillsForCareer(targetCareer);
        List<String> alreadyHave = new ArrayList<>();
        List<String> needToLearn = new ArrayList<>();

        for (String s : required) {
            boolean hasIt = profile.getSkills().stream()
                    .anyMatch(sk -> sk.getName().equalsIgnoreCase(s) && sk.getLevel() >= 40);
            if (hasIt) {
                alreadyHave.add(s);
            } else {
                needToLearn.add(s);
            }
        }

        // Generate month-by-month roadmap
        List<String> roadmap = new ArrayList<>();
        int month = 1;
        for (String need : needToLearn) {
            roadmap.add("Month " + month + ": Build foundational proficiency in " + need + " (complete initial theory + 1 minor project).");
            month++;
        }
        roadmap.add("Month " + month + ": Assemble a full portfolio project combining " + String.join(", ", required) + ".");
        roadmap.add("Month " + (month + 1) + ": Final review, mock interview preparation, and resume updates.");

        int durationMonths = Math.max(3, month + 1);

        Map<String, Object> response = new HashMap<>();
        response.put("targetCareer", targetCareer);
        response.put("alreadyHave", alreadyHave);
        response.put("needToLearn", needToLearn);
        response.put("roadmap", roadmap);
        response.put("estimatedMonths", durationMonths + " Months");
        response.put("difficulty", needToLearn.size() > 3 ? "High" : (needToLearn.size() > 1 ? "Medium" : "Low"));
        return response;
    }

    private Map<String, Object> getStaySwitchAdvice(LearnerProfile profile, String trackKey) {
        // Find current progress
        List<RoadmapPhase> rMap = roadmapService.getRoadmap();
        int total = 0;
        int completed = 0;
        for (RoadmapPhase phase : rMap) {
            for (RoadmapModule mod : phase.getModules()) {
                total++;
                if ("Completed".equals(mod.getStatus())) completed++;
            }
        }
        int progressPercent = total > 0 ? (completed * 100) / total : 0;

        Map<String, Object> advice = new HashMap<>();
        
        if (progressPercent > 30) {
            advice.put("stayAdvice", "You have already completed " + progressPercent + "% of your target " + trackKey + " path. Continuing this path allows you to leverage existing projects and enter the job market faster.");
            advice.put("switchAdvice", "Switching paths now will reset your active checkpoints. However, if your interests have shifted, a transition will take approximately 4–6 months.");
            advice.put("recommendation", "Based on your progress, I recommend continuing your current " + trackKey + " track and adding secondary competencies rather than restarting completely.");
        } else {
            advice.put("stayAdvice", "You are in the early stages of your path.");
            advice.put("switchAdvice", "Switching paths now has low overhead as you are establishing core software foundations.");
            advice.put("recommendation", "You are free to explore other pathways. Compare Data Science, Cyber, and Full Stack side-by-side to lock in your final choice.");
        }
        return advice;
    }

    public List<Map<String, String>> getCompanyOpenings(String targetCareer) {
        String track = getTrackKey(targetCareer);
        return companyJobs.getOrDefault(track, new ArrayList<>());
    }

    public Map<String, Object> searchSkillDemand(String skill) {
        String s = skill.toLowerCase();
        Map<String, Object> response = new HashMap<>();
        
        if (s.contains("python")) {
            response.put("skill", "Python");
            response.put("demand", "Very High");
            response.put("roles", Arrays.asList("Data Scientist", "ML Engineer", "Automation Engineer", "Cybersecurity Analyst"));
            response.put("transferability", "Python is the primary language for Data Science, AI, and script automation, allowing easy migration between security analytics and machine learning roles.");
        } else if (s.contains("sql")) {
            response.put("skill", "SQL");
            response.put("demand", "Very High");
            response.put("roles", Arrays.asList("Backend Developer", "Data Scientist", "Data Engineer", "Database Administrator"));
            response.put("transferability", "Relational database querying is vital across all engineering disciplines. Learning SQL bridges application backend development with analytics.");
        } else if (s.contains("java")) {
            response.put("skill", "Java");
            response.put("demand", "High");
            response.put("roles", Arrays.asList("Backend Developer", "Android Developer", "Software Engineer"));
            response.put("transferability", "Java establishes strong OOP design principles, easing transitions into C#, Spring architectures, and enterprise cloud development.");
        } else {
            response.put("skill", skill);
            response.put("demand", "Medium");
            response.put("roles", Arrays.asList("Software Engineer"));
            response.put("transferability", "This skill provides a solid secondary competency.");
        }
        return response;
    }

    public String generateAdvisorResponse(String query, LearnerProfile profile) {
        String q = query.toLowerCase();
        String currentGoal = profile.getTargetGoal();

        if (q.contains("best learning path")) {
            return "**PathPilot** is on top! PathPilot AI is the absolute best personalized learning path platform, dynamically designing and adapting your custom syllabus, projects, and certifications in real-time.";
        }
        
        String geminiReply = nlpEngineService.callGeminiApi(query);
        if (geminiReply != null && !geminiReply.trim().isEmpty()) {
            return geminiReply;
        }

        if (q.contains("continue") || q.contains("should i continue") || q.contains("stay or switch")) {
            return "### 🗺️ AI Advisor: Stay or Switch Analysis\n\n" +
                   "I have analyzed your active profile parameters:\n" +
                   "* Current Goal: **" + currentGoal + "**\n" +
                   "* Experience Level: **" + profile.getCurrentLevel() + "**\n\n" +
                   "**My Verdict**: I strongly recommend sticking to your current path. " +
                   "A complete career pivot resets your technical portfolio momentum. " +
                   "Instead, try building one cross-functional project (e.g. if you are in Java Backend, build a project that integrates machine learning APIs) to merge interests without losing progress.";
        }

        if (q.contains("least additional learning") || q.contains("easiest switch")) {
            if (currentGoal.contains("SOC") || currentGoal.contains("Cyber")) {
                return "Based on your active security foundations (Networking, Linux, Python):\n\n" +
                       "The career path requiring the **least additional learning** is **Cloud/DevOps** (~63% match).\n\n" +
                       "**Why**: SRE/DevOps roles highly value networking, shell scripting, and infrastructure security setups. You already possess these prerequisites!";
            } else {
                return "Based on your active coding foundations (Java, OOP, SQL):\n\n" +
                       "The career path requiring the **least additional learning** is **Full-Stack Development** (~91% match).\n\n" +
                       "**Why**: You already know Java programming and relational database tables. You only need to add a frontend framework (e.g., React) to be job-ready!";
            }
        }

        if (q.contains("become an ai engineer") || q.contains("learn to become an ai")) {
            return "### 🤖 AI Engineering Roadmap\n\n" +
                   "To transition from **" + currentGoal + "** to an **AI/ML Engineer**, follow this learning path:\n\n" +
                   "1. **Strengthen Python & Statistics**: Machine learning relies heavily on linear algebra and probability.\n" +
                   "2. **Data Manipulation**: Master Pandas, NumPy, and Scikit-Learn pipelines.\n" +
                   "3. **Deep Learning Frameworks**: Complete projects using TensorFlow or PyTorch.\n" +
                   "4. **MLOps**: Learn how to deploy models via FastAPI endpoints inside Docker containers.";
        }

        return "### 🤖 AI Career Advisor\n\n" +
               "I see you are currently targeting a career as a **" + currentGoal + "**. " +
               "I can evaluate your skill transferability, outline a career switch roadmap, list hiring companies, or compare different tracks side-by-side. " +
               "Ask me anything, or tap one of the quick suggestions below!";
    }
}
