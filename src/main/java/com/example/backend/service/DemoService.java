package com.example.backend.service;

import com.example.backend.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DemoService {

    public LearnerProfile getDemoProfile(String demoId) {
        if ("soc".equalsIgnoreCase(demoId)) {
            return LearnerProfile.builder()
                    .name("Alex (SOC Candidate)")
                    .targetGoal("Cybersecurity SOC Analyst")
                    .currentLevel("Intermediate")
                    .skills(new ArrayList<>(Arrays.asList(
                            new SkillNode("Python", 80, "Advanced"),
                            new SkillNode("Linux", 70, "Intermediate"),
                            new SkillNode("Networking", 20, "Beginner"),
                            new SkillNode("SIEM", 0, "None"),
                            new SkillNode("Log Analysis", 10, "Beginner"),
                            new SkillNode("MITRE ATT&CK", 0, "None"),
                            new SkillNode("Threat Detection", 5, "Beginner"),
                            new SkillNode("Incident Response", 0, "None")
                    )))
                    .strongSkills(new ArrayList<>(Arrays.asList("Python Scripting", "Linux Directory Structure")))
                    .weakSkills(new ArrayList<>(Arrays.asList("Networking Fundamentals", "SIEM Splunk queries", "Log parsing")))
                    .completedLearning(new ArrayList<>(Arrays.asList("Python for Beginners", "Linux Administration Basics")))
                    .previousProjects(new ArrayList<>(Arrays.asList("File backup script in Bash", "Simple port scanner in Python")))
                    .interests(new ArrayList<>(Arrays.asList("Cybersecurity", "Network Forensics", "Ethical Hacking")))
                    .preferredLearningStyle("Hands-on projects")
                    .availableTime("8 hours/week")
                    .targetCompletionPeriod("3 Months")
                    .xp(200)
                    .streak(3)
                    .assessmentAverage(0)
                    .completedModulesCount(0)
                    .completedProjectsCount(0)
                    .recentActivities(new ArrayList<>(Arrays.asList("Selected SOC Analyst Demo Profile", "Parsed existing Python and Linux experience")))
                    .badges(new ArrayList<>(Arrays.asList("Cyber Explorer", "Bash Scripter")))
                    .build();
        } else if ("java".equalsIgnoreCase(demoId)) {
            return LearnerProfile.builder()
                    .name("Devin (Java Dev)")
                    .targetGoal("Java Backend Developer")
                    .currentLevel("Intermediate")
                    .skills(new ArrayList<>(Arrays.asList(
                            new SkillNode("Java", 80, "Advanced"),
                            new SkillNode("OOP", 85, "Advanced"),
                            new SkillNode("SQL", 20, "Beginner"),
                            new SkillNode("Spring Boot", 15, "Beginner"),
                            new SkillNode("REST APIs", 10, "Beginner"),
                            new SkillNode("JPA / Hibernate", 0, "None"),
                            new SkillNode("Spring Security", 0, "None")
                    )))
                    .strongSkills(new ArrayList<>(Arrays.asList("Core Java", "Object-Oriented Design (OOD)")))
                    .weakSkills(new ArrayList<>(Arrays.asList("SQL & Database Joins", "Spring IoC Container", "REST API standards")))
                    .completedLearning(new ArrayList<>(Arrays.asList("Java Core Basics", "OOP Design Patterns")))
                    .previousProjects(new ArrayList<>(Arrays.asList("Console-based CRUD application", "Basic calculator app")))
                    .interests(new ArrayList<>(Arrays.asList("Software Architecture", "API Design", "Distributed Systems")))
                    .preferredLearningStyle("Interactive practice")
                    .availableTime("10 hours/week")
                    .targetCompletionPeriod("4 Months")
                    .xp(300)
                    .streak(5)
                    .assessmentAverage(0)
                    .completedModulesCount(0)
                    .completedProjectsCount(0)
                    .recentActivities(new ArrayList<>(Arrays.asList("Selected Java Developer Demo Profile", "Verified Java Core competency")))
                    .badges(new ArrayList<>(Arrays.asList("Java Initiate", "OOP Master")))
                    .build();
        } else { // default to data science
            return LearnerProfile.builder()
                    .name("Sam (Data Aspirant)")
                    .targetGoal("Data Scientist")
                    .currentLevel("Intermediate")
                    .skills(new ArrayList<>(Arrays.asList(
                            new SkillNode("Python", 75, "Advanced"),
                            new SkillNode("Statistics", 80, "Advanced"),
                            new SkillNode("Machine Learning", 10, "Beginner"),
                            new SkillNode("Data Visualization", 30, "Beginner"),
                            new SkillNode("Deep Learning", 0, "None"),
                            new SkillNode("Model Deployment", 0, "None")
                    )))
                    .strongSkills(new ArrayList<>(Arrays.asList("Python Core", "Probability & Hypothesis Testing")))
                    .weakSkills(new ArrayList<>(Arrays.asList("Scikit-Learn algorithms", "Pandas data cleansing", "Model Evaluation Metrics")))
                    .completedLearning(new ArrayList<>(Arrays.asList("Python for Analytics", "Mathematical Statistics")))
                    .previousProjects(new ArrayList<>(Arrays.asList("Statistical Survey on store sales", "Linear Regression from scratch")))
                    .interests(new ArrayList<>(Arrays.asList("Data Science", "Predictive Analytics", "Deep Learning")))
                    .preferredLearningStyle("Mixed")
                    .availableTime("6 hours/week")
                    .targetCompletionPeriod("3 Months")
                    .xp(150)
                    .streak(2)
                    .assessmentAverage(0)
                    .completedModulesCount(0)
                    .completedProjectsCount(0)
                    .recentActivities(new ArrayList<>(Arrays.asList("Selected Data Scientist Demo Profile", "Verified Stats prerequisites")))
                    .badges(new ArrayList<>(Arrays.asList("Data Tracker", "Stats Geek")))
                    .build();
        }
    }

    public List<RoadmapPhase> getDemoRoadmap(String demoId) {
        List<RoadmapPhase> phases = new ArrayList<>();
        if ("soc".equalsIgnoreCase(demoId)) {
            // Phase 1: Networking
            RoadmapModule networking = RoadmapModule.builder()
                    .id("soc_mod1")
                    .topic("Networking Fundamentals")
                    .description("Learn essential networking components including IP addressing, subnets, routers, switches, and the OSI model.")
                    .estimatedDuration("45 minutes")
                    .difficulty("Beginner")
                    .prerequisites(Collections.emptyList())
                    .whyRecommended("You already have intermediate Linux knowledge, so Linux fundamentals were skipped. Networking was identified as your biggest skill gap and is a prerequisite for understanding SOC traffic analysis.")
                    .status("Available")
                    .objectives(Arrays.asList("Understand IP addresses & subnets", "Understand TCP/UDP protocols", "Understand DNS mechanism", "Understand common network ports"))
                    .practiceTask("Configure a mock local network configuration and verify routing tables, then audit standard ports on a host.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res1").title("Networking Essentials for Cybersecurity").type("Video").provider("YouTube").duration("20m").url("https://www.youtube.com").difficulty("Beginner").build(),
                            Resource.builder().id("res2").title("TCP/IP Protocol Suite Guide").type("Article").provider("Mozilla MDN / TechTarget").duration("15m").url("https://developer.mozilla.org").difficulty("Beginner").build(),
                            Resource.builder().id("res3").title("Hands-on Packet Analysis with Wireshark").type("Exercise").provider("TryHackMe").duration("30m").url("https://tryhackme.com").difficulty("Intermediate").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q1").type("MCQ").questionText("Which protocol operates at the Transport Layer of the OSI model and guarantees packet delivery?")
                                    .options(Arrays.asList("UDP", "IP", "TCP", "HTTP")).correctOptionIndex(2).build(),
                            RoadmapModule.Question.builder().id("q2").type("MCQ").questionText("What is the primary function of DNS (Domain Name System)?")
                                    .options(Arrays.asList("Encrypt traffic", "Resolve domain names to IP addresses", "Filter malicious packets", "Assign DHCP leases")).correctOptionIndex(1).build(),
                            RoadmapModule.Question.builder().id("q3").type("Scenario").questionText("You notice alert logs showing traffic sent to destination port 22. What protocol is this traffic utilizing and should it be public-facing?")
                                    .correctAnswer("SSH (Secure Shell), which should generally not be exposed to the public internet without secure access controls (like VPN or restricted IPs) as it allows remote terminal access.").build()
                    ))
                    .build();

            phases.add(RoadmapPhase.builder()
                    .id("soc_phase1")
                    .title("Phase 1 — Networking Foundation")
                    .description("Master basic packet routing and ports before analyzing attacks.")
                    .status("In_Progress")
                    .modules(new ArrayList<>(Arrays.asList(networking)))
                    .build());

            // Phase 2: Log Analysis
            RoadmapModule logAnalysis = RoadmapModule.builder()
                    .id("soc_mod2")
                    .topic("Linux & Windows Log Analysis")
                    .description("Learn how to inspect Windows Event logs and Linux syslog files to locate failed logons, process creations, and system alerts.")
                    .estimatedDuration("60 minutes")
                    .difficulty("Intermediate")
                    .prerequisites(Arrays.asList("Networking Fundamentals"))
                    .whyRecommended("Required to understand system events. Since you know Linux commands, we focus on log schemas and audit policies.")
                    .status("Locked")
                    .objectives(Arrays.asList("Read syslog & auth.log on Linux", "Filter Event Viewer logs using Event IDs", "Understand system log severity ratings"))
                    .practiceTask("Parse a raw auth.log file using grep/awk to isolate IPs with multiple failed SSH login attempts.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res4").title("Windows Event Log Analysis Reference").type("Documentation").provider("Microsoft").duration("20m").url("https://learn.microsoft.com").difficulty("Intermediate").build(),
                            Resource.builder().id("res5").title("Log Parsing with Bash Utilities").type("Exercise").provider("SadServers").duration("25m").url("https://sadservers.com").difficulty("Intermediate").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q4").type("MCQ").questionText("On Windows systems, what Event ID represents a successful logon?")
                                    .options(Arrays.asList("4624", "4625", "4720", "7045")).correctOptionIndex(0).build(),
                            RoadmapModule.Question.builder().id("q5").type("MCQ").questionText("Where are authentication logs typically stored on a Debian/Ubuntu Linux system?")
                                    .options(Arrays.asList("/var/log/syslog", "/var/log/auth.log", "/etc/auth.conf", "/var/adm/messages")).correctOptionIndex(1).build()
                    ))
                    .build();

            phases.add(RoadmapPhase.builder()
                    .id("soc_phase2")
                    .title("Phase 2 — System Auditing")
                    .description("Learn where operating systems log activity and how to audit them.")
                    .status("Locked")
                    .modules(new ArrayList<>(Arrays.asList(logAnalysis)))
                    .build());

            // Phase 3: SIEM
            RoadmapModule siem = RoadmapModule.builder()
                    .id("soc_mod3")
                    .topic("SIEM Fundamentals")
                    .description("Learn SIEM architecture and query syntax using Splunk Search Processing Language (SPL).")
                    .estimatedDuration("90 minutes")
                    .difficulty("Intermediate")
                    .prerequisites(Arrays.asList("Linux & Windows Log Analysis"))
                    .whyRecommended("A SOC Analyst requires SIEM capabilities to aggregate and query massive log volumes in production environments.")
                    .status("Locked")
                    .objectives(Arrays.asList("Understand SIEM collection & indexing", "Write basic Splunk SPL queries", "Create alerts and dashboard panels"))
                    .practiceTask("Write a Splunk SPL query to group login failures by user IP and trigger an alert if the count exceeds 10 in 5 minutes.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res6").title("Splunk Fundamentals 1").type("Course").provider("Splunk").duration("60m").url("https://splunk.com").difficulty("Intermediate").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q6").type("MCQ").questionText("Which Splunk command is used to summarize search results in a tabular format?")
                                    .options(Arrays.asList("search", "table", "stats", "eval")).correctOptionIndex(2).build()
                    ))
                    .build();

            phases.add(RoadmapPhase.builder()
                    .id("soc_phase3")
                    .title("Phase 3 — SIEM & Alerting")
                    .description("Aggregate logs and search them programmatically.")
                    .status("Locked")
                    .modules(new ArrayList<>(Arrays.asList(siem)))
                    .build());

            // Phase 4: MITRE & Advanced
            RoadmapModule mitre = RoadmapModule.builder()
                    .id("soc_mod4")
                    .topic("MITRE ATT&CK Mapping")
                    .description("Learn to categorize security events and threat intelligence using the MITRE ATT&CK framework matrix.")
                    .estimatedDuration("60 minutes")
                    .difficulty("Intermediate")
                    .prerequisites(Arrays.asList("SIEM Fundamentals"))
                    .whyRecommended("Provides standard lexicon and threat intelligence mapping for categorizing real-world hacker tactics and techniques.")
                    .status("Locked")
                    .objectives(Arrays.asList("Differentiate Tactics and Techniques", "Use MITRE Navigator to map detections", "Correlate SIEM rules with ATT&CK tags"))
                    .practiceTask("Map a documented spearphishing malware campaign details to specific MITRE techniques.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res7").title("MITRE ATT&CK Academy").type("Course").provider("MITRE Engenuity").duration("45m").url("https://mitre-engenuity.org").difficulty("Beginner").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q7").type("MCQ").questionText("Under the MITRE ATT&CK matrix, 'Persistence' refers to:")
                                    .options(Arrays.asList("Gaining administrative rights", "Maintaining a foothold in the environment across restarts", "Bypassing defenses", "Stealing credentials")).correctOptionIndex(1).build()
                    ))
                    .build();

            RoadmapModule threatDetection = RoadmapModule.builder()
                    .id("soc_mod5")
                    .topic("Threat Detection & Incident Response")
                    .description("Learn detection engineering using Sigma/YARA and incident triage procedures.")
                    .estimatedDuration("120 minutes")
                    .difficulty("Advanced")
                    .prerequisites(Arrays.asList("MITRE ATT&CK Mapping"))
                    .whyRecommended("Incident Response is the culmination of security operations. Analysts must quarantine threats and mitigate damages.")
                    .status("Locked")
                    .objectives(Arrays.asList("Understand NIST Incident Handling lifecycle", "Formulate basic threat containment strategies", "Understand detection rule syntax"))
                    .practiceTask("Triage a simulated workstation compromise alert and write down isolation steps.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res8").title("Incident Handler's Handbook").type("Article").provider("SANS Institute").duration("30m").url("https://sans.org").difficulty("Advanced").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q8").type("MCQ").questionText("What is the first step in the NIST incident response lifecycle after preparation?")
                                    .options(Arrays.asList("Containment", "Eradication", "Detection & Analysis", "Post-Incident Activity")).correctOptionIndex(2).build()
                    ))
                    .build();

            phases.add(RoadmapPhase.builder()
                    .id("soc_phase4")
                    .title("Phase 4 — Threat Operations")
                    .description("Leverage frameworks and detection rules to respond to attacks.")
                    .status("Locked")
                    .modules(new ArrayList<>(Arrays.asList(mitre, threatDetection)))
                    .build());

            // Phase 5: Project
            RoadmapModule capstone = RoadmapModule.builder()
                    .id("soc_mod6")
                    .topic("Final SOC Capstone Project")
                    .description("Investigate an active ransomware simulation in a virtual cyber range, extract IOCs, and generate an incident report.")
                    .estimatedDuration("180 minutes")
                    .difficulty("Advanced")
                    .prerequisites(Arrays.asList("Threat Detection & Incident Response"))
                    .whyRecommended("Consolidates all modular skills (logs, SIEM, MITRE mapping, response) into a single portfolio-ready project.")
                    .status("Locked")
                    .objectives(Arrays.asList("Investigate multi-stage host compromise", "Isolate ransomware artifacts", "Draft executive incident summaries"))
                    .practiceTask("Analyze provided PCAP and Sysmon logs for the incident, compile a list of Indicators of Compromise (IOCs), and write a standard Incident Report.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res9").title("Writing Effective Incident Reports").type("Documentation").provider("CISA").duration("20m").url("https://cisa.gov").difficulty("Intermediate").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q9").type("Scenario").questionText("Describe the key components you would include in your executive summary for a ransomware incident report.")
                                    .correctAnswer("Include: Date/time of detection, scope of impact (number of systems encrypted), critical assets compromised, root cause vector (e.g. phishing email), actions taken to contain, and status of recovery operations.").build()
                    ))
                    .build();

            phases.add(RoadmapPhase.builder()
                    .id("soc_phase5")
                    .title("Phase 5 — Capstone Project")
                    .description("Perform a full cyber range investigation and draft an executive report.")
                    .status("Locked")
                    .modules(new ArrayList<>(Arrays.asList(capstone)))
                    .build());

        } else if ("java".equalsIgnoreCase(demoId)) {
            // Java Backend Developer Roadmap
            RoadmapModule database = RoadmapModule.builder()
                    .id("java_mod1")
                    .topic("SQL & Databases")
                    .description("Learn SQL syntax, aggregate functions, INNER/OUTER joins, indices, and schema design.")
                    .estimatedDuration("60 minutes")
                    .difficulty("Beginner")
                    .prerequisites(Collections.emptyList())
                    .whyRecommended("Since you have strong Java and OOP foundations, database operations were prioritized as your core entry gap for backend roles.")
                    .status("Available")
                    .objectives(Arrays.asList("Write SELECT statements with WHERE, GROUP BY, and HAVING", "Execute INNER and LEFT joins", "Create primary and foreign key constraints"))
                    .practiceTask("Write queries on a mock employee database to list departments where average salaries exceed $80,000.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res_j1").title("SQL Crash Course for Developers").type("Video").provider("YouTube").duration("25m").url("https://www.youtube.com").difficulty("Beginner").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q_j1").type("MCQ").questionText("Which database join returns all rows from the left table, and matching rows from the right table?")
                                    .options(Arrays.asList("INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL JOIN")).correctOptionIndex(1).build()
                    ))
                    .build();

            phases.add(RoadmapPhase.builder()
                    .id("java_phase1")
                    .title("Phase 1 — Data Storage")
                    .description("Master relational databases and queries.")
                    .status("In_Progress")
                    .modules(new ArrayList<>(Arrays.asList(database)))
                    .build());

            RoadmapModule springCore = RoadmapModule.builder()
                    .id("java_mod2")
                    .topic("Spring Core Framework")
                    .description("Understand Dependency Injection (DI), Inversion of Control (IoC), Beans, and Application Context.")
                    .estimatedDuration("60 minutes")
                    .difficulty("Intermediate")
                    .prerequisites(Arrays.asList("SQL & Databases"))
                    .whyRecommended("Spring Core is the prerequisite engine behind all Spring Boot functionality.")
                    .status("Locked")
                    .objectives(Arrays.asList("Define Bean configuration", "Inject dependencies via constructor", "Explain IoC container lifecycle"))
                    .practiceTask("Refactor a manual Java object creation process to utilize Spring @Component and @Autowired dependency injection.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res_j2").title("Spring Dependency Injection Explained").type("Article").provider("Baeldung").duration("15m").url("https://baeldung.com").difficulty("Intermediate").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q_j2").type("MCQ").questionText("What is the default scope of a Spring Bean?")
                                    .options(Arrays.asList("Prototype", "Request", "Session", "Singleton")).correctOptionIndex(3).build()
                    ))
                    .build();

            RoadmapModule springBoot = RoadmapModule.builder()
                    .id("java_mod3")
                    .topic("Spring Boot REST APIs")
                    .description("Learn Spring Boot configuration, building controllers, handling HTTP requests, mapping JSON.")
                    .estimatedDuration("90 minutes")
                    .difficulty("Intermediate")
                    .prerequisites(Arrays.asList("Spring Core Framework"))
                    .whyRecommended("Primary backend framework topic. You must learn to configure HTTP controllers to serve clients.")
                    .status("Locked")
                    .objectives(Arrays.asList("Configure a Spring Boot application", "Create @RestController endpoints", "Handle Query params and Path variables"))
                    .practiceTask("Build a Controller that returns, updates, and deletes items in a catalog over HTTP REST conventions.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res_j3").title("Building REST Services with Spring Boot").type("Course").provider("Spring Guides").duration("45m").url("https://spring.io").difficulty("Intermediate").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q_j3").type("MCQ").questionText("Which annotation is used to bind an HTTP GET request to a handler method?")
                                    .options(Arrays.asList("@PostMapping", "@GetMapping", "@RestController", "@RequestMapping")).correctOptionIndex(1).build()
                    ))
                    .build();

            phases.add(RoadmapPhase.builder()
                    .id("java_phase2")
                    .title("Phase 2 — Web API Development")
                    .description("Transition Java skills to the web using Spring Boot.")
                    .status("Locked")
                    .modules(new ArrayList<>(Arrays.asList(springCore, springBoot)))
                    .build());

            // Phase 3: ORM & JPA
            RoadmapModule hibernateJPA = RoadmapModule.builder()
                    .id("java_mod4")
                    .topic("Spring Data JPA & Hibernate")
                    .description("Learn ORM mapping, Entity relationships, repository interfaces, JPQL.")
                    .estimatedDuration("90 minutes")
                    .difficulty("Intermediate")
                    .prerequisites(Arrays.asList("Spring Boot REST APIs"))
                    .whyRecommended("Required to wire Spring Boot controllers to relational databases seamlessly using Object-Relational Mapping.")
                    .status("Locked")
                    .objectives(Arrays.asList("Map relational entities using @Entity", "Define One-to-Many relationships", "Extend CrudRepository / JpaRepository"))
                    .practiceTask("Set up an Order database mapping where each customer has many orders, and write queries to find orders by customer name.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res_j4").title("Spring Data JPA Tutorial").type("Course").provider("Java Guides").duration("60m").url("https://javaguides.net").difficulty("Intermediate").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q_j4").type("MCQ").questionText("What does JPA stand for?")
                                    .options(Arrays.asList("Java Persistence API", "Java Program Association", "JSON Parser Adapter", "Joint Process Architecture")).correctOptionIndex(0).build()
                    ))
                    .build();

            RoadmapModule springSecurity = RoadmapModule.builder()
                    .id("java_mod5")
                    .topic("Spring Security & JWT")
                    .description("Secure REST APIs with authentication filters, RBAC, and JSON Web Tokens.")
                    .estimatedDuration("120 minutes")
                    .difficulty("Advanced")
                    .prerequisites(Arrays.asList("Spring Data JPA & Hibernate"))
                    .whyRecommended("Security is mandatory in enterprise backends. You must learn to secure your endpoints and authenticate requests.")
                    .status("Locked")
                    .objectives(Arrays.asList("Configure SecurityFilterChain", "Implement JWT token creation and validation", "Secure endpoints with role checks"))
                    .practiceTask("Implement custom auth filters that intercept headers, validate JWT signatures, and assign roles.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res_j5").title("JWT Authentication in Spring Boot").type("Video").provider("YouTube").duration("45m").url("https://youtube.com").difficulty("Advanced").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q_j5").type("MCQ").questionText("Which header is standard for transmitting a JWT bearer token in HTTP requests?")
                                    .options(Arrays.asList("Authentication", "Authorization", "Bearer", "Token-Signature")).correctOptionIndex(1).build()
                    ))
                    .build();

            phases.add(RoadmapPhase.builder()
                    .id("java_phase3")
                    .title("Phase 3 — Enterprise Architecture")
                    .description("Add persistence models and secure them with production security policies.")
                    .status("Locked")
                    .modules(new ArrayList<>(Arrays.asList(hibernateJPA, springSecurity)))
                    .build());

        } else {
            // Data Scientist Roadmap
            RoadmapModule dataViz = RoadmapModule.builder()
                    .id("ds_mod1")
                    .topic("Data Manipulation & Visualization")
                    .description("Learn Pandas data cleaning, group aggregations, Seaborn correlation plots, and outlier detection.")
                    .estimatedDuration("60 minutes")
                    .difficulty("Beginner")
                    .prerequisites(Collections.emptyList())
                    .whyRecommended("Since you have strong Python and statistics skills, we start directly with data manipulation libraries before ML modeling.")
                    .status("Available")
                    .objectives(Arrays.asList("Load and clean CSV data using Pandas", "Join and filter datasets", "Generate heatmaps and distributions using Seaborn"))
                    .practiceTask("Clean a dirty sales database, handle missing values, and output a distribution plot of sales frequencies.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res_d1").title("Pandas & Seaborn Masterclass").type("Course").provider("Kaggle Learn").duration("40m").url("https://kaggle.com").difficulty("Beginner").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q_d1").type("MCQ").questionText("Which Pandas function is used to handle missing null values in a DataFrame?")
                                    .options(Arrays.asList("dropna() / fillna()", "replace_null()", "clean()", "remove()")).correctOptionIndex(0).build()
                    ))
                    .build();

            phases.add(RoadmapPhase.builder()
                    .id("ds_phase1")
                    .title("Phase 1 — Analytics Core")
                    .description("Clean datasets and represent correlation coefficients visually.")
                    .status("In_Progress")
                    .modules(new ArrayList<>(Arrays.asList(dataViz)))
                    .build());

            RoadmapModule machineLearning = RoadmapModule.builder()
                    .id("ds_mod2")
                    .topic("Supervised Machine Learning")
                    .description("Implement Regression, Decision Trees, SVMs, and model evaluation metrics like MSE, R2, F1-Score.")
                    .estimatedDuration("90 minutes")
                    .difficulty("Intermediate")
                    .prerequisites(Arrays.asList("Data Manipulation & Visualization"))
                    .whyRecommended("Fundamental Machine Learning module. You will learn to train models on historical features and validate predictions.")
                    .status("Locked")
                    .objectives(Arrays.asList("Understand Linear vs Logistic Regression", "Train Decision Trees with Scikit-Learn", "Evaluate performance using confusion matrices"))
                    .practiceTask("Train a Random Forest classifier in Scikit-Learn to predict customer churn and report its F1-Score.")
                    .recommendedResources(Arrays.asList(
                            Resource.builder().id("res_d2").title("Scikit-Learn ML Intro").type("Article").provider("Towards Data Science").duration("20m").url("https://towardsdatascience.com").difficulty("Intermediate").build()
                    ))
                    .assessmentQuestions(Arrays.asList(
                            RoadmapModule.Question.builder().id("q_d2").type("MCQ").questionText("Which metric is most appropriate for evaluating a classification model dealing with imbalanced target labels?")
                                    .options(Arrays.asList("Accuracy", "Mean Squared Error", "F1-Score", "R-Squared")).correctOptionIndex(2).build()
                    ))
                    .build();

            phases.add(RoadmapPhase.builder()
                    .id("ds_phase2")
                    .title("Phase 2 — Machine Learning")
                    .description("Train supervised models and evaluate prediction matrices.")
                    .status("Locked")
                    .modules(new ArrayList<>(Arrays.asList(machineLearning)))
                    .build());
        }

        return phases;
    }
}
