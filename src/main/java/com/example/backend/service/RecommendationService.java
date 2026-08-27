package com.example.backend.service;

import com.example.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RoadmapService roadmapService;

    private final List<LearningResource> resourcesDb = new ArrayList<>();

    public RecommendationService() {
        seedResources();
    }

    private void seedResources() {
        // --- CYBERSECURITY / SOC ANALYST RESOURCES ---
        // Free
        resourcesDb.add(LearningResource.builder()
                .id("res_soc_f1")
                .title("Networking Essentials for Cybersecurity")
                .platform("YouTube")
                .type("YouTube Playlists")
                .url("https://www.youtube.com/playlist?list=PLIhvC56v6F8-Y-KLSycgALHC0gyK2o39F")
                .instructor("NetworkChuck")
                .skill("Networking")
                .difficulty("Beginner")
                .duration("12 hours")
                .rating(4.9)
                .price(0)
                .isFree(true)
                .currency("₹")
                .thumbnail("https://img.youtube.com/vi/S7MNX_UD7QI/0.jpg")
                .description("Get ready to learn networking! This is your primary course for TCP/IP, OSI model, subnetting, and network protocols.")
                .build());

        resourcesDb.add(LearningResource.builder()
                .id("res_soc_f2")
                .title("TCP/IP and Subnetting Tutorial")
                .platform("YouTube")
                .type("YouTube Videos")
                .url("https://www.youtube.com/watch?v=5WfiTHiU4x8")
                .instructor("PowerCert Animated Videos")
                .skill("Networking")
                .difficulty("Beginner")
                .duration("45 minutes")
                .rating(4.8)
                .price(0)
                .isFree(true)
                .currency("₹")
                .thumbnail("https://img.youtube.com/vi/5WfiTHiU4x8/0.jpg")
                .description("An animated breakdown of TCP/IP, common network protocols, ports, packets, and how subnets route traffic.")
                .build());

        resourcesDb.add(LearningResource.builder()
                .id("res_soc_f3")
                .title("Official Wireshark Packet Analysis Guide")
                .platform("Wireshark Org")
                .type("Documentation")
                .url("https://www.wireshark.org/docs/")
                .instructor("Wireshark Core Team")
                .skill("Networking")
                .difficulty("Intermediate")
                .duration("4 hours")
                .rating(4.6)
                .price(0)
                .isFree(true)
                .currency("₹")
                .thumbnail("")
                .description("The official documentation and user guides for decoding packet capture files and tracing malicious network streams.")
                .build());

        resourcesDb.add(LearningResource.builder()
                .id("res_soc_f4")
                .title("Splunk Boss of the SOC (BOTS) Lab Exercises")
                .platform("Splunk")
                .type("Practice/Assessment")
                .url("https://www.splunk.com")
                .instructor("Splunk Security Team")
                .skill("SIEM")
                .difficulty("Intermediate")
                .duration("10 hours")
                .rating(4.8)
                .price(0)
                .isFree(true)
                .currency("₹")
                .thumbnail("")
                .description("Realistic, hands-on practice labs simulating active corporate breach investigations. Write Splunk SPL queries to hunt threats.")
                .build());

        // Low Cost
        resourcesDb.add(LearningResource.builder()
                .id("res_soc_l1")
                .title("Windows Event Log Analysis Bootcamp")
                .platform("Udemy")
                .type("Online Courses")
                .url("https://www.udemy.com")
                .instructor("Security Academy")
                .skill("Log Analysis")
                .difficulty("Intermediate")
                .duration("5 hours")
                .rating(4.6)
                .price(389)
                .isFree(false)
                .currency("₹")
                .thumbnail("")
                .description("Master Windows Event Viewer, Event IDs, Sysmon logs, and auditing policies to detect unauthorized login attempts.")
                .build());

        // Medium Cost
        resourcesDb.add(LearningResource.builder()
                .id("res_soc_m1")
                .title("CompTIA Network+ Complete Course & Exam Prep")
                .platform("Udemy")
                .type("Online Courses")
                .url("https://www.udemy.com")
                .instructor("Jason Dion")
                .skill("Networking")
                .difficulty("Intermediate")
                .duration("24 hours")
                .rating(4.8)
                .price(1299)
                .isFree(false)
                .currency("₹")
                .thumbnail("")
                .description("A comprehensive guide to networking protocols, network architecture, security configurations, and cabling standards.")
                .build());

        // Premium Cost
        resourcesDb.add(LearningResource.builder()
                .id("res_soc_p1")
                .title("Google Cybersecurity Professional Certificate")
                .platform("Coursera")
                .type("Certification Courses")
                .url("https://www.coursera.org/professional-certificates/google-cybersecurity")
                .instructor("Google Career Certificates")
                .skill("Threat Detection")
                .difficulty("Beginner")
                .duration("6 months")
                .rating(4.8)
                .price(3200)
                .isFree(false)
                .currency("₹")
                .thumbnail("")
                .description("Prepare for an entry-level job in cybersecurity. Learn Python, Linux, SQL, SIEM tools, and threat mapping models.")
                .build());


        // --- JAVA DEVELOPER RESOURCES ---
        // Free
        resourcesDb.add(LearningResource.builder()
                .id("res_java_f1")
                .title("Java Programming for Beginners")
                .platform("freeCodeCamp")
                .type("Online Courses")
                .url("https://www.freecodecamp.org")
                .instructor("Ania Kubow")
                .skill("Java")
                .difficulty("Beginner")
                .duration("8 hours")
                .rating(4.8)
                .price(0)
                .isFree(true)
                .currency("₹")
                .thumbnail("https://img.youtube.com/vi/grEKM2i3yRY/0.jpg")
                .description("Learn core Java syntax, variables, conditional logic, functions, loops, and basic file storage.")
                .build());

        resourcesDb.add(LearningResource.builder()
                .id("res_java_f2")
                .title("Java Object Oriented Programming (OOP)")
                .platform("YouTube")
                .type("YouTube Videos")
                .url("https://www.youtube.com")
                .instructor("Kunal Kushwaha")
                .skill("OOP")
                .difficulty("Intermediate")
                .duration("3 hours")
                .rating(4.9)
                .price(0)
                .isFree(true)
                .currency("₹")
                .thumbnail("")
                .description("In-depth lectures on inheritance, polymorphism, encapsulation, abstraction, interfaces, and packages in Java.")
                .build());

        resourcesDb.add(LearningResource.builder()
                .id("res_java_f3")
                .title("SQL and Relational Database Queries Tutorial")
                .platform("YouTube")
                .type("YouTube Playlists")
                .url("https://www.youtube.com")
                .instructor("Programming with Mosh")
                .skill("SQL")
                .difficulty("Beginner")
                .duration("4 hours")
                .rating(4.8)
                .price(0)
                .isFree(true)
                .currency("₹")
                .thumbnail("")
                .description("Learn relational database schemas, JOINs, WHERE statements, aggregations, and subqueries.")
                .build());

        // Low / Medium Cost
        resourcesDb.add(LearningResource.builder()
                .id("res_java_m1")
                .title("Spring Boot 3 & Spring Framework 6 Masterclass")
                .platform("Udemy")
                .type("Online Courses")
                .url("https://www.udemy.com")
                .instructor("Chad Darby")
                .skill("Spring Boot")
                .difficulty("Intermediate")
                .duration("36 hours")
                .rating(4.9)
                .price(799)
                .isFree(false)
                .currency("₹")
                .thumbnail("")
                .description("Build real REST APIs with Spring Boot, Hibernate ORM, JPA repositories, Spring Security, and Maven configurations.")
                .build());

        resourcesDb.add(LearningResource.builder()
                .id("res_java_m2")
                .title("Spring Boot Developer Roadmap & Practice")
                .platform("JetBrains Academy")
                .type("Interactive Coding Courses")
                .url("https://hyperskill.org")
                .instructor("JetBrains Team")
                .skill("REST APIs")
                .difficulty("Intermediate")
                .duration("40 hours")
                .rating(4.7)
                .price(1800)
                .isFree(false)
                .currency("₹")
                .thumbnail("")
                .description("Hands-on interactive Java backend path. Code inside the IDE, build full web controllers, and pass integrated tests.")
                .build());

        // Premium Cost
        resourcesDb.add(LearningResource.builder()
                .id("res_java_p1")
                .title("Java Backend Developer Bootcamp")
                .platform("Udacity")
                .type("Certification Courses")
                .url("https://www.udacity.com")
                .instructor("Udacity Experts")
                .skill("Java")
                .difficulty("Advanced")
                .duration("4 months")
                .rating(4.6)
                .price(8500)
                .isFree(false)
                .currency("₹")
                .thumbnail("")
                .description("An intensive career-readiness bootcamp. Cover REST, persistence, microservices architectures, authorization protocols, and API security.")
                .build());


        // --- DATA SCIENCE RESOURCES ---
        // Free
        resourcesDb.add(LearningResource.builder()
                .id("res_ds_f1")
                .title("Pandas & NumPy for Data Manipulation")
                .platform("Kaggle Learn")
                .type("Interactive Coding Courses")
                .url("https://www.kaggle.com/learn")
                .instructor("Kaggle Experts")
                .skill("Data Visualization")
                .difficulty("Beginner")
                .duration("5 hours")
                .rating(4.8)
                .price(0)
                .isFree(true)
                .currency("₹")
                .thumbnail("")
                .description("Interactive coding environment. Clean retail datasets, merge dataframes, locate outliers, and plot distributions.")
                .build());

        resourcesDb.add(LearningResource.builder()
                .id("res_ds_f2")
                .title("Machine Learning Crash Course")
                .platform("Google Developers")
                .type("Online Courses")
                .url("https://developers.google.com/machine-learning/crash-course")
                .instructor("Google Research")
                .skill("Machine Learning")
                .difficulty("Intermediate")
                .duration("15 hours")
                .rating(4.9)
                .price(0)
                .isFree(true)
                .currency("₹")
                .thumbnail("")
                .description("Google's fast-paced introduction to machine learning. Explore loss functions, linear regression, neural network models.")
                .build());

        resourcesDb.add(LearningResource.builder()
                .id("res_ds_f3")
                .title("Statistics and Probability Handbook")
                .platform("OpenStax")
                .type("Documentation")
                .url("https://openstax.org")
                .instructor("Dr. Barbara Illowsky")
                .skill("Statistics")
                .difficulty("Beginner")
                .duration("12 hours")
                .rating(4.7)
                .price(0)
                .isFree(true)
                .currency("₹")
                .thumbnail("")
                .description("An open-source textbook covering central limit theorems, normal distributions, hypothesis testing, and regressions.")
                .build());

        // Medium Cost
        resourcesDb.add(LearningResource.builder()
                .id("res_ds_m1")
                .title("Machine Learning A-Z: Python & R in Data Science")
                .platform("Udemy")
                .type("Online Courses")
                .url("https://www.udemy.com")
                .instructor("Kirill Eremenko")
                .skill("Machine Learning")
                .difficulty("Intermediate")
                .duration("44 hours")
                .rating(4.8)
                .price(999)
                .isFree(false)
                .currency("₹")
                .thumbnail("")
                .description("Build regression models, classification predictions, clustering (K-Means, Hierarchical), and deep neural networks in Python.")
                .build());

        // Premium Cost
        resourcesDb.add(LearningResource.builder()
                .id("res_ds_p1")
                .title("Professional Certificate in Data Science")
                .platform("edX / Harvard")
                .type("Certification Courses")
                .url("https://www.edx.org")
                .instructor("Rafael Irizarry")
                .skill("Machine Learning")
                .difficulty("Advanced")
                .duration("1 year")
                .rating(4.9)
                .price(18500)
                .isFree(false)
                .currency("₹")
                .thumbnail("")
                .description("Learn R, data visualization, probability, inference, linear models, machine learning, and Capstone projects.")
                .build());
    }

    public List<LearningResource> getRecommendations(
            LearnerProfile profile, 
            List<RoadmapPhase> roadmap, 
            int maxBudget, 
            String typeFilter, 
            String search, 
            String sortBy, 
            String skillFilter, 
            String difficultyFilter, 
            String platformFilter) {

        // 1. Identify the current roadmap stage (active module)
        RoadmapModule activeModule = null;
        for (RoadmapPhase phase : roadmap) {
            for (RoadmapModule module : phase.getModules()) {
                if ("In_Progress".equals(module.getStatus()) || "Available".equals(module.getStatus())) {
                    activeModule = module;
                    break;
                }
            }
            if (activeModule != null) break;
        }

        final RoadmapModule currentModule = activeModule;
        
        // 2. Loop and score relevance
        List<LearningResource> results = new ArrayList<>();
        
        for (LearningResource res : resourcesDb) {
            // Budget check (Maximum budget rule)
            if (res.getPrice() > maxBudget) {
                continue; // Skip paid resources above budget
            }

            // Copy resource to set dynamic scoring fields
            LearningResource scored = LearningResource.builder()
                    .id(res.getId())
                    .title(res.getTitle())
                    .platform(res.getPlatform())
                    .type(res.getType())
                    .url(res.getUrl())
                    .thumbnail(res.getThumbnail())
                    .instructor(res.getInstructor())
                    .skill(res.getSkill())
                    .difficulty(res.getDifficulty())
                    .duration(res.getDuration())
                    .rating(res.getRating())
                    .price(res.getPrice())
                    .isFree(res.isFree())
                    .currency(res.getCurrency())
                    .description(res.getDescription())
                    .build();

            int score = 10; // baseline relevance

            // Match current active module
            if (currentModule != null && isSkillMatching(scored.getSkill(), currentModule.getTopic())) {
                score += 60;
                scored.setWhyRecommended("Highly recommended because you are currently working on the '" + currentModule.getTopic() + "' milestone in your roadmap.");
            } 
            // Match weak skills / gaps
            else if (profile != null && isWeakSkill(scored.getSkill(), profile.getWeakSkills())) {
                score += 30;
                scored.setWhyRecommended("Recommended because " + scored.getSkill() + " is identified as one of your target skill gaps for " + profile.getTargetGoal() + ".");
            }
            // Match overall goal track
            else if (profile != null && isSkillRelevantForGoal(scored.getSkill(), profile.getTargetGoal())) {
                score += 15;
                scored.setWhyRecommended("Recommended to build secondary competencies for a career as a " + profile.getTargetGoal() + ".");
            } else {
                scored.setWhyRecommended("Recommended to broaden your overall computing and system foundations.");
            }

            // Match difficulty preference
            if (profile != null && profile.getCurrentLevel().equalsIgnoreCase(scored.getDifficulty())) {
                score += 10;
            }

            // Rating weight
            score += (int) (scored.getRating() * 2);

            scored.setRelevanceScore(score);
            results.add(scored);
        }

        // 3. Apply Filters
        // Type filter
        if (typeFilter != null && !"All".equalsIgnoreCase(typeFilter)) {
            results = results.stream()
                    .filter(r -> r.getType().equalsIgnoreCase(typeFilter))
                    .collect(Collectors.toList());
        }

        // Search filter
        if (search != null && !search.trim().isEmpty()) {
            String q = search.toLowerCase();
            results = results.stream()
                    .filter(r -> r.getTitle().toLowerCase().contains(q) 
                              || r.getInstructor().toLowerCase().contains(q) 
                              || r.getDescription().toLowerCase().contains(q)
                              || r.getSkill().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        // Skill Filter
        if (skillFilter != null && !"All".equalsIgnoreCase(skillFilter)) {
            results = results.stream()
                    .filter(r -> r.getSkill().equalsIgnoreCase(skillFilter))
                    .collect(Collectors.toList());
        }

        // Difficulty Filter
        if (difficultyFilter != null && !"All".equalsIgnoreCase(difficultyFilter)) {
            results = results.stream()
                    .filter(r -> r.getDifficulty().equalsIgnoreCase(difficultyFilter))
                    .collect(Collectors.toList());
        }

        // Platform Filter
        if (platformFilter != null && !"All".equalsIgnoreCase(platformFilter)) {
            results = results.stream()
                    .filter(r -> r.getPlatform().equalsIgnoreCase(platformFilter))
                    .collect(Collectors.toList());
        }

        // 4. Sort results
        if ("Rating".equalsIgnoreCase(sortBy)) {
            results.sort((r1, r2) -> Double.compare(r2.getRating(), r1.getRating()));
        } else if ("Price".equalsIgnoreCase(sortBy)) {
            results.sort(Comparator.comparingInt(LearningResource::getPrice));
        } else if ("Duration".equalsIgnoreCase(sortBy)) {
            results.sort(Comparator.comparing(LearningResource::getDuration)); // simplified duration sort
        } else {
            // Default: Relevance Sort
            results.sort((r1, r2) -> Integer.compare(r2.getRelevanceScore(), r1.getRelevanceScore()));
        }

        return results;
    }

    private boolean isSkillMatching(String skill, String topic) {
        String s = skill.toLowerCase();
        String t = topic.toLowerCase();
        return t.contains(s) || s.contains(t);
    }

    private boolean isWeakSkill(String skill, List<String> weakSkills) {
        if (weakSkills == null) return false;
        String s = skill.toLowerCase();
        for (String w : weakSkills) {
            if (w.toLowerCase().contains(s)) return true;
        }
        return false;
    }

    private boolean isSkillRelevantForGoal(String skill, String goal) {
        String g = goal.toLowerCase();
        String s = skill.toLowerCase();
        
        if (g.contains("soc") || g.contains("cyber") || g.contains("security")) {
            return s.equals("networking") || s.equals("linux") || s.equals("siem") || s.equals("log analysis") || s.equals("threat detection") || s.equals("incident response");
        }
        if (g.contains("java") || g.contains("backend") || g.contains("spring")) {
            return s.equals("java") || s.equals("oop") || s.equals("sql") || s.equals("spring boot") || s.equals("rest apis");
        }
        if (g.contains("data") || g.contains("science") || g.contains("ml") || g.contains("stats") || g.contains("scientist")) {
            return s.equals("python") || s.equals("statistics") || s.equals("machine learning") || s.equals("data visualization");
        }
        return false;
    }
}
