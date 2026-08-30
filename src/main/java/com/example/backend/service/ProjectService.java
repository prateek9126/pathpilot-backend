package com.example.backend.service;

import com.example.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private NlpEngineService nlpEngineService;

    private final List<ProjectRecommendation> projectDb = new ArrayList<>();
    private final Map<String, ProjectRecommendation> activeProjects = new HashMap<>();

    public ProjectService() {
        seedProjects();
    }

    private void seedProjects() {
        // ==================== CYBERSECURITY / SOC PROJECTS ====================
        projectDb.add(ProjectRecommendation.builder()
                .id("proj_soc_1")
                .name("AI-Powered SOC Threat Detection Dashboard")
                .category("Cybersecurity SOC Analyst")
                .description("Build a security correlation dashboard that parses system logs, alerts on MITRE ATT&CK patterns, and uses a local model to flag anomalies.")
                .difficulty("Advanced")
                .duration("2-3 weeks")
                .portfolioValue("Very High")
                .whyRecommended("You have intermediate Linux skills and basic Python. This project bridges your coding background with threat intelligence mapping.")
                .requiredSkills(Arrays.asList("Python", "Linux", "SIEM", "Log Analysis", "MITRE ATT&CK", "Threat Detection"))
                .skillsToImprove(Arrays.asList("Log Correlation", "Dashboard Design", "Threat Hunting"))
                .technologyStack(Map.of("Backend", "Python (Flask/FastAPI)", "SIEM Engine", "Elasticsearch / Splunk", "Frontend", "React (Tailwind)", "Machine Learning", "Scikit-Learn"))
                .benefits(Arrays.asList("Demonstrates threat indexing capability", "Provides portfolio-grade dashboard design", "Combines ML with security logs"))
                .basicVersion("A simple script that parses auth.log for failed login attempts and displays them in a table.")
                .advancedVersion("Aggregate syslog, Event Viewer, and authentication logs into an ELK stack, using SPL-like queries to generate visual alerts.")
                .uniqueVersion("AI-Powered correlation: Integrate a lightweight anomaly detector that groups logs and predicts if they represent an active lateral movement campaign.")
                .mvpFeatures(Arrays.asList("Log parsing engine", "Failed logon alerts", "Basic UI layout"))
                .advancedFeatures(Arrays.asList("Elasticsearch indexing", "MITRE tactic tagging", "Threat feed correlation"))
                .aiFeatures(Arrays.asList("Log anomaly scoring", "Predictive alert severity classification"))
                .uniqueFeatures(Arrays.asList("Interactive visual network graph of alert nodes", "One-click workstation quarantine trigger"))
                .roadmap(Arrays.asList(
                        "Phase 1 - Planning: Define log sources (syslog, sysmon) and map core MITRE tactical alerts.",
                        "Phase 2 - Setup: Install Elasticsearch and set up the Flask API structure.",
                        "Phase 3 - Development: Build log parser modules and wire the React dashboard panels.",
                        "Phase 4 - Testing: Replay brute force and lateral movement log samples to verify alert triggers.",
                        "Phase 5 - Deployment: Containerize the setup using Docker Compose.",
                        "Phase 6 - Portfolio: Record a walkthrough video demonstrating attack detection and document in README."
                ))
                .build());

        projectDb.add(ProjectRecommendation.builder()
                .id("proj_soc_2")
                .name("Phishing URL Intelligence & Detection System")
                .category("Cybersecurity SOC Analyst")
                .description("Create a threat response utility that parses emails, extracts URLs, queries reputation databases, and sandbox-detonates URLs.")
                .difficulty("Intermediate")
                .duration("1-2 weeks")
                .portfolioValue("High")
                .whyRecommended("Leverages your advanced Python scripting capabilities to build an automated incident triage tool.")
                .requiredSkills(Arrays.asList("Python", "Networking", "Log Analysis", "Threat Detection", "Incident Response"))
                .skillsToImprove(Arrays.asList("API Integration", "Email Header Parsing", "IOC Management"))
                .technologyStack(Map.of("Scripting", "Python", "API Services", "VirusTotal / URLVoid", "Database", "SQLite", "Frontend", "React"))
                .benefits(Arrays.asList("Builds automation playbook knowledge", "Improves SMTP and DNS protocol understanding", "Creates a practical utility for security teams"))
                .basicVersion("A Python CLI tool that takes a URL, checks it against Google Safe Browsing API, and outputs a threat score.")
                .advancedVersion("Build a full web app that parses uploaded .eml files, extracts headers, IPs, and attachments, and outputs VirusTotal reputation scores.")
                .uniqueVersion("Sandbox Integration: Automatically spin up a headless browser container, capture a screenshot of the URL rendering, and audit its certificates in isolation.")
                .mvpFeatures(Arrays.asList("Email header parser", "VirusTotal API lookup", "Basic threat report UI"))
                .advancedFeatures(Arrays.asList("Heuristics parser for spoofed headers", "SQLite database of flagged malicious IOCs"))
                .aiFeatures(Arrays.asList("NLP phishing text classifier for email bodies"))
                .uniqueFeatures(Arrays.asList("Automatic screenshot grabber of destination page", "Mock mail server interface for triage simulation"))
                .roadmap(Arrays.asList(
                        "Phase 1 - Planning: Define email headers to parse (SPF, DKIM) and choose reputation APIs.",
                        "Phase 2 - Setup: Set up project folder, configure SQLite DB, and register API tokens.",
                        "Phase 3 - Development: Write Python MIME parsing code and React header visualization cards.",
                        "Phase 4 - Testing: Parse real spam emails and verify reputation scoring accuracy.",
                        "Phase 5 - Deployment: Deploy on a local sandbox with Docker.",
                        "Phase 6 - Portfolio: Publish project source on GitHub with example reports."
                ))
                .build());

        projectDb.add(ProjectRecommendation.builder()
                .id("proj_soc_3")
                .name("Threat Intelligence Feed Aggregator")
                .category("Cybersecurity SOC Analyst")
                .description("Build a feed aggregator that scrapes IOCs (IPs, hashes, domains) from open threat feeds, deduplicates them, and exports them in STIX/TAXII format.")
                .difficulty("Beginner")
                .duration("1 week")
                .portfolioValue("Medium")
                .whyRecommended("A great entry-level project to understand IOC ingestion and threat intelligence sharing frameworks.")
                .requiredSkills(Arrays.asList("Python", "Log Analysis", "Threat Detection"))
                .skillsToImprove(Arrays.asList("Data Parsing", "XML/JSON Serialization", "STIX Formats"))
                .technologyStack(Map.of("Backend", "Python", "Database", "PostgreSQL / SQLite", "Data Feeds", "AlienVault OTX / Abuse.ch"))
                .benefits(Arrays.asList("Teaches IOC lifecycle", "Improves data scraping skills", "Understands threat intelligence sharing schemas"))
                .basicVersion("A script that downloads text files of malicious IPs and prints them.")
                .advancedVersion("An automated collector that pulls from 5+ feeds hourly, saves them in SQLite, and has a web interface to search by IP.")
                .uniqueVersion("STIX 2.1 Export: Expose a certified TAXII server endpoint allowing external SIEMs to subscribe and query active threat indicators.")
                .mvpFeatures(Arrays.asList("Scraper cron scheduler", "Duplicate filtering", "Search feed page"))
                .advancedFeatures(Arrays.asList("Auto tag threat categories", "Export to CSV/JSON"))
                .aiFeatures(Arrays.asList("Predicting confidence score of feeds based on historical accuracy"))
                .uniqueFeatures(Arrays.asList("STIX 2.1 JSON mapper and visual graph viewer"))
                .roadmap(Arrays.asList(
                        "Phase 1 - Planning: Identify open source intelligence (OSINT) feeds and map STIX models.",
                        "Phase 2 - Setup: Configure DB schema and install feed python SDKs.",
                        "Phase 3 - Development: Implement collector cron scripts and API search routers.",
                        "Phase 4 - Testing: Validate STIX export schema compliance using official validators.",
                        "Phase 5 - Deployment: Deploy collector on a local server.",
                        "Phase 6 - Portfolio: Document instructions for SIEM integration in GitHub README."
                ))
                .build());

        projectDb.add(ProjectRecommendation.builder()
                .id("proj_soc_4")
                .name("IOC Investigation Platform")
                .category("Cybersecurity SOC Analyst")
                .requiredSkills(Arrays.asList("Python", "Log Analysis", "Threat Detection", "Incident Response"))
                .difficulty("Intermediate").duration("2 weeks").portfolioValue("High").build());
        projectDb.add(ProjectRecommendation.builder()
                .id("proj_soc_5")
                .name("Lightweight Network Intrusion Detection Sensor")
                .category("Cybersecurity SOC Analyst")
                .requiredSkills(Arrays.asList("Python", "Networking", "Log Analysis", "Threat Detection"))
                .difficulty("Advanced").duration("3 weeks").portfolioValue("Very High").build());
        projectDb.add(ProjectRecommendation.builder()
                .id("proj_soc_6")
                .name("Distributed Security Log Collector & Analyzer")
                .category("Cybersecurity SOC Analyst")
                .requiredSkills(Arrays.asList("Linux", "Python", "Networking", "Log Analysis"))
                .difficulty("Intermediate").duration("2 weeks").portfolioValue("High").build());


        // ==================== JAVA BACKEND PROJECTS ====================
        projectDb.add(ProjectRecommendation.builder()
                .id("proj_java_1")
                .name("E-Banking Security Ledger & API")
                .category("Java Backend Developer")
                .description("Build a secure financial transactions ledger backend featuring strict authentication, audit logs, and double-entry transaction integrity.")
                .difficulty("Advanced")
                .duration("2-3 weeks")
                .portfolioValue("Very High")
                .whyRecommended("Since you have solid Java core skills, this project will teach you enterprise Spring Security, ACID transactions, and JPA persistence.")
                .requiredSkills(Arrays.asList("Java", "OOP", "SQL", "Spring Boot", "REST APIs", "JPA / Hibernate", "Spring Security"))
                .skillsToImprove(Arrays.asList("Transaction Management", "Auditing Policies", "JWT Token Signing"))
                .technologyStack(Map.of("Language", "Java 21", "Framework", "Spring Boot", "Security", "Spring Security + JWT", "ORM", "Hibernate / Spring Data JPA", "Database", "PostgreSQL / H2"))
                .benefits(Arrays.asList("Demonstrates robust enterprise security", "Proves understanding of financial transaction safety", "JPA relationships mapping implementation"))
                .basicVersion("A Java console CRUD app that maintains balances and transfers between accounts stored in memory.")
                .advancedVersion("Spring Boot REST API with full JWT login, database persistence, transaction locks, and audit logger tracking IP and actions.")
                .uniqueVersion("Secure ledger: Store transactions with cryptographic hashes linking records sequentially, preventing data tampering in the database.")
                .mvpFeatures(Arrays.asList("User Registration & Login", "Account creation", "Fund transfers REST endpoint"))
                .advancedFeatures(Arrays.asList("Spring Security JWT filter", "Database constraint locks for negative balances"))
                .aiFeatures(Arrays.asList("AI Fraud Detection: Anomaly classification model looking for rapid high-value transactions"))
                .uniqueFeatures(Arrays.asList("Cryptographic transaction block chain hash verification", "Detailed PDF bank statement generator"))
                .roadmap(Arrays.asList(
                        "Phase 1 - Planning: Draw DB schema (accounts, ledger) and define spring security filter chain.",
                        "Phase 2 - Setup: Initialize Spring project with JPA, H2, Security, and Lombok.",
                        "Phase 3 - Development: Build Entities, JpaRepositories, Services with @Transactional, and Controllers.",
                        "Phase 4 - Testing: Write JUnit tests for concurrent balance updates and security filters.",
                        "Phase 5 - Deployment: Deploy application locally and configure docker container.",
                        "Phase 6 - Portfolio: Publish code to GitHub with complete REST endpoint documentation."
                ))
                .build());

        projectDb.add(ProjectRecommendation.builder()
                .id("proj_java_2")
                .name("E-Commerce Microservices Backend")
                .category("Java Backend Developer")
                .description("Build a cloud-native e-commerce API consisting of microservices for Catalog, Orders, Payments, and Notifications connected via API Gateway.")
                .difficulty("Advanced")
                .duration("3 weeks")
                .portfolioValue("Very High")
                .whyRecommended("Essential for understanding modern distributed backend architectures and Spring Cloud service patterns.")
                .requiredSkills(Arrays.asList("Java", "Spring Boot", "REST APIs", "JPA / Hibernate", "SQL"))
                .skillsToImprove(Arrays.asList("Distributed Systems", "Service Discovery", "Message Brokers"))
                .technologyStack(Map.of("Framework", "Spring Boot / Spring Cloud", "Database", "PostgreSQL", "Message Queue", "RabbitMQ", "Service Registry", "Eureka", "API Gateway", "Spring Cloud Gateway"))
                .benefits(Arrays.asList("Proves microservices engineering capacity", "Teaches inter-service communication (REST, RPC, MQ)", "Builds container orchestration skills"))
                .basicVersion("A monolithic Spring Boot API managing items, cart, and order tables in a single DB schema.")
                .advancedVersion("Split into 3 separate services with Eureka registry, Api-gateway, and Feign Clients for communication.")
                .uniqueVersion("Resilience Patterns: Add circuit breaker patterns (Resilience4j) and RabbitMQ event broker for async payment notification handling.")
                .mvpFeatures(Arrays.asList("Product service API", "Order placement service", "Eureka registry discovery"))
                .advancedFeatures(Arrays.asList("API gateway routing", "Spring Cloud Config server"))
                .aiFeatures(Arrays.asList("AI-driven stock optimization forecasting tool"))
                .uniqueFeatures(Arrays.asList("RabbitMQ asynchronous order queues", "Resilience4j fallback circuit breaker"))
                .roadmap(Arrays.asList(
                        "Phase 1 - Planning: Design microservices boundary map and event flow (OrderPlaced, PaymentCharged).",
                        "Phase 2 - Setup: Create Maven parent-module structure with Eureka server and gateway configurations.",
                        "Phase 3 - Development: Implement separate microservices, databases, and Feign client clients.",
                        "Phase 4 - Testing: Test resilience thresholds (killing product service) to verify gateway fallback.",
                        "Phase 5 - Deployment: Package all services using Docker Compose.",
                        "Phase 6 - Portfolio: Map architectures and post Docker scripts in GitHub."
                ))
                .build());

        projectDb.add(ProjectRecommendation.builder()
                .id("proj_java_3")
                .name("Library Catalog & Borrowing REST API")
                .category("Java Backend Developer")
                .description("Build a RESTful web API for managing books, authors, and student borrowing profiles. Features validation, paging, and custom database queries.")
                .difficulty("Beginner")
                .duration("1 week")
                .portfolioValue("Medium")
                .whyRecommended("Perfect first project to transition console Java skills to the web using Spring MVC and basic JPA.")
                .requiredSkills(Arrays.asList("Java", "OOP", "SQL", "Spring Boot", "REST APIs", "JPA / Hibernate"))
                .skillsToImprove(Arrays.asList("Spring Webmvc Controller mapping", "Hibernate DB creation schemas", "REST API Conventions"))
                .technologyStack(Map.of("Language", "Java", "Framework", "Spring Boot", "Database", "H2 (In-memory)", "ORM", "Spring Data JPA"))
                .benefits(Arrays.asList("Teaches REST CRUD routing", "Understands basic JPA entity mapping", "Validates input requests"))
                .basicVersion("A Java console program where library books are written to a CSV text file.")
                .advancedVersion("Spring Boot web server where books are mapped to database entities and CRUD operations are exposed on HTTP endpoints.")
                .uniqueVersion("Interactive Library Catalog: Add search auto-suggest endpoints with database indices and implement borrowing loan date checks.")
                .mvpFeatures(Arrays.asList("CRUD for Books & Authors", "Borrow request endpoint", "Simple validations"))
                .advancedFeatures(Arrays.asList("Paging & sorting on book search", "Global exception handler controller advice"))
                .aiFeatures(Arrays.asList("Simple category categorization algorithm based on book description"))
                .uniqueFeatures(Arrays.asList("Automatic return deadline notification triggers", "Swagger-UI documentation panel"))
                .roadmap(Arrays.asList(
                        "Phase 1 - Planning: Map entity schema: One-to-Many (Author-Books) and Many-to-Many.",
                        "Phase 2 - Setup: Initialize Spring project with Web, H2, and JPA.",
                        "Phase 3 - Development: Write entity models, repositories, and MVC controllers.",
                        "Phase 4 - Testing: Validate mock HTTP GET/POST endpoints using Postman.",
                        "Phase 5 - Deployment: Run backend server locally.",
                        "Phase 6 - Portfolio: Commit clean source code to GitHub and add API call screenshots."
                ))
                .build());

        projectDb.add(ProjectRecommendation.builder()
                .id("proj_java_4")
                .name("Expense Analyzer & Split API")
                .category("Java Backend Developer")
                .requiredSkills(Arrays.asList("Java", "SQL", "Spring Boot", "REST APIs", "JPA / Hibernate"))
                .difficulty("Intermediate").duration("1-2 weeks").portfolioValue("High").build());
        projectDb.add(ProjectRecommendation.builder()
                .id("proj_java_5")
                .name("Student Registration Management System")
                .category("Java Backend Developer")
                .requiredSkills(Arrays.asList("Java", "OOP", "SQL", "Spring Boot"))
                .difficulty("Beginner").duration("1 week").portfolioValue("Medium").build());
        projectDb.add(ProjectRecommendation.builder()
                .id("proj_java_6")
                .name("Corporate Job Portal API")
                .category("Java Backend Developer")
                .requiredSkills(Arrays.asList("Java", "SQL", "Spring Boot", "REST APIs", "JPA / Hibernate", "Spring Security"))
                .difficulty("Intermediate").duration("2 weeks").portfolioValue("High").build());


        // ==================== DATA SCIENCE PROJECTS ====================
        projectDb.add(ProjectRecommendation.builder()
                .id("proj_ds_1")
                .name("Predictive Customer Churn Classifier")
                .category("Data Scientist")
                .description("Train and deploy a classification model that predicts if a telecom customer is likely to cancel their subscription based on usage patterns.")
                .difficulty("Intermediate")
                .duration("1-2 weeks")
                .portfolioValue("High")
                .whyRecommended("Combines your statistics background with supervised ML algorithms to solve a classic business analytics problem.")
                .requiredSkills(Arrays.asList("Python", "Statistics", "Machine Learning", "Data Visualization"))
                .skillsToImprove(Arrays.asList("Feature Engineering", "Model Evaluation Metrics", "Classification Algorithms"))
                .technologyStack(Map.of("Programming", "Python", "Data Libraries", "Pandas, NumPy", "Model training", "Scikit-Learn (Random Forest, XGBoost)", "Visualizations", "Matplotlib / Seaborn"))
                .benefits(Arrays.asList("Teaches features correlation analysis", "Demonstrates evaluation metrics choice", "Improves data preprocessing skills"))
                .basicVersion("A linear regression analysis showing correlation between pricing and subscription cancellations.")
                .advancedVersion("Load consumer churn datasets into Pandas, handle missing data, encode categorical variables, and train a Scikit-Learn Random Forest.")
                .uniqueVersion("Interactive Prediction Dashboard: Build a Web UI where users can input customer metrics and get an instant risk percentage.")
                .mvpFeatures(Arrays.asList("Data cleaning pipeline", "Scikit-learn model training", "F1/Accuracy reports"))
                .advancedFeatures(Arrays.asList("Hyperparameter tuning with GridSearchCV", "SMOTE dataset class balancing"))
                .aiFeatures(Arrays.asList("Feature importance analysis tracking top churn indicators"))
                .uniqueFeatures(Arrays.asList("REST API endpoint for batch predictions", "Seaborn churn distribution heatmap charts"))
                .roadmap(Arrays.asList(
                        "Phase 1 - Planning: Download customer dataset, formulate metrics questions, and choose ML algorithms.",
                        "Phase 2 - Setup: Set up Jupyter Notebook and import pandas, numpy, and sklearn.",
                        "Phase 3 - Development: Preprocess dataset features, train classifier, and save model using joblib.",
                        "Phase 4 - Testing: Validate metrics on test fold (Confusion matrix, ROC-AUC curve).",
                        "Phase 5 - Deployment: Expose model through a simple FastAPI endpoint.",
                        "Phase 6 - Portfolio: Publish Notebook file containing evaluation graphs on GitHub."
                ))
                .build());

        projectDb.add(ProjectRecommendation.builder()
                .id("proj_ds_2")
                .name("Real-time Sentiment Analysis Dashboard")
                .category("Data Scientist")
                .description("Build a streaming analytics platform that ingests live social media text, performs NLP sentiment analysis, and displays trends on a live dashboard.")
                .difficulty("Advanced")
                .duration("2 weeks")
                .portfolioValue("Very High")
                .whyRecommended("Teaches NLP text classification, API polling, and real-time visualization dashboards.")
                .requiredSkills(Arrays.asList("Python", "Machine Learning", "Data Visualization"))
                .skillsToImprove(Arrays.asList("Natural Language Processing", "Streamlit Dashboarding", "Text Preprocessing"))
                .technologyStack(Map.of("Language", "Python", "NLP Library", "NLTK / HuggingFace Transformers", "Frontend Dashboard", "Streamlit", "API Data Source", "Reddit / Twitter Scrapers"))
                .benefits(Arrays.asList("Shows data scraping competency", "Demonstrates real-time dashboard engineering", "Teaches tokenization and sentiment mapping"))
                .basicVersion("A script that reads a text file of sentences and prints sentiment scores (positive/negative).")
                .advancedVersion("A Streamlit dashboard that scrapes hot Reddit posts in real time, applies NLTK, and plots sentiment bar charts.")
                .uniqueVersion("Transformers Translation: Ingest multilingual posts, translate them via a MarianMT model, and track sentiment swings over hot topics.")
                .mvpFeatures(Arrays.asList("Text tokenizer scraper", "Sentiment scoring model", "Live text search bar"))
                .advancedFeatures(Arrays.asList("Trend mapping (charts showing sentiment over time)", "Deduplicate posts"))
                .aiFeatures(Arrays.asList("HuggingFace RoBERTa model integration for contextual sentiment classification"))
                .uniqueFeatures(Arrays.asList("Automatic toxic words filter", "Export analysis reports to CSV"))
                .roadmap(Arrays.asList(
                        "Phase 1 - Planning: Define target API (Reddit), map data structures, and check tokenizer libraries.",
                        "Phase 2 - Setup: Register API developer keys and write basic streamer templates.",
                        "Phase 3 - Development: Implement text cleaning filters, sentiment model wrappers, and Streamlit layout.",
                        "Phase 4 - Testing: Input complex, sarcastic sentences and monitor sentiment accuracy.",
                        "Phase 5 - Deployment: Deploy Streamlit app on Streamlit Cloud.",
                        "Phase 6 - Portfolio: Share the live dashboard link on LinkedIn and resume."
                ))
                .build());

        projectDb.add(ProjectRecommendation.builder()
                .id("proj_ds_3")
                .name("Housing Price Prediction Model")
                .category("Data Scientist")
                .description("Implement linear and ensemble regression models to estimate real estate prices based on geographical and building attributes.")
                .difficulty("Beginner")
                .duration("1 week")
                .portfolioValue("Medium")
                .whyRecommended("Essential introduction to regression analysis, feature scaling, and performance diagnostics.")
                .requiredSkills(Arrays.asList("Python", "Statistics", "Machine Learning"))
                .skillsToImprove(Arrays.asList("Feature Selection", "Regression diagnostics", "Data scaling"))
                .technologyStack(Map.of("Language", "Python", "Library", "Scikit-Learn", "Visuals", "Seaborn"))
                .benefits(Arrays.asList("Builds regression foundation", "Teaches multi-collinearity checks", "Evaluates predictions via RMSE"))
                .basicVersion("A script calculating basic correlations between house size and prices.")
                .advancedVersion("A clean pipeline loading house data, performing feature scaling, training Ridge/Lasso models, and plotting residuals.")
                .uniqueVersion("Interactive Map Plotting: Integrate geopy to map housing coordinates and plot heatmaps of prices by region.")
                .mvpFeatures(Arrays.asList("Feature scaling scaler", "Linear Regression training", "RMSE report"))
                .advancedFeatures(Arrays.asList("Lasso/Ridge regression comparisons", "R-squared metrics calculations"))
                .aiFeatures(Arrays.asList("Feature weight analysis pinpointing price drivers"))
                .uniqueFeatures(Arrays.asList("Visual Residual Plot viewer panel"))
                .roadmap(Arrays.asList(
                        "Phase 1 - Planning: Analyze datasets and understand target parameters (price).",
                        "Phase 2 - Setup: Configure environment with Scikit-learn.",
                        "Phase 3 - Development: Code data preprocessing, regression training, and residual calculation modules.",
                        "Phase 4 - Testing: Validate metrics on validation dataset folds.",
                        "Phase 5 - Deployment: Save model model binary.",
                        "Phase 6 - Portfolio: Publish Jupyter Notebook files with visualization plots on GitHub."
                ))
                .build());

        projectDb.add(ProjectRecommendation.builder()
                .id("proj_ds_4")
                .name("Predictive Equipment Maintenance Sensor Tool")
                .category("Data Scientist")
                .requiredSkills(Arrays.asList("Python", "Statistics", "Machine Learning"))
                .difficulty("Advanced").duration("2-3 weeks").portfolioValue("Very High").build());
        projectDb.add(ProjectRecommendation.builder()
                .id("proj_ds_5")
                .name("Product Recommendation Engine")
                .category("Data Scientist")
                .requiredSkills(Arrays.asList("Python", "Machine Learning", "Data Visualization"))
                .difficulty("Intermediate").duration("2 weeks").portfolioValue("High").build());
        projectDb.add(ProjectRecommendation.builder()
                .id("proj_ds_6")
                .name("Fake News Classifier")
                .category("Data Scientist")
                .requiredSkills(Arrays.asList("Python", "Statistics", "Machine Learning"))
                .difficulty("Beginner").duration("1 week").portfolioValue("Medium").build());
    }

    public List<ProjectRecommendation> getRecommendations(LearnerProfile profile) {
        String goal = profile.getTargetGoal().toLowerCase();
        String cat = "soc"; // default
        if (goal.contains("java") || goal.contains("backend") || goal.contains("spring")) {
            cat = "Java Backend Developer";
        } else if (goal.contains("data") || goal.contains("scientist") || goal.contains("machine") || goal.contains("ds")) {
            cat = "Data Scientist";
        } else {
            cat = "Cybersecurity SOC Analyst";
        }

        final String finalCat = cat;
        List<ProjectRecommendation> results = projectDb.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(finalCat))
                .collect(Collectors.toList());

        // Process skill mapping dynamically for the profile
        for (ProjectRecommendation proj : results) {
            mapProjectSkills(proj, profile);
        }

        return results;
    }

    public ProjectRecommendation getProject(String projectId, LearnerProfile profile) {
        ProjectRecommendation proj = activeProjects.get(projectId);
        if (proj == null) {
            // Find in database
            ProjectRecommendation raw = projectDb.stream()
                    .filter(p -> p.getId().equals(projectId))
                    .findFirst()
                    .orElse(null);
            
            if (raw == null) return null;
            
            // Clone/build new instance to preserve session state
            proj = cloneProject(raw);
            mapProjectSkills(proj, profile);
            activeProjects.put(projectId, proj);
        } else {
            // update skills dynamically in case they changed during active session
            mapProjectSkills(proj, profile);
        }
        return proj;
    }

    private ProjectRecommendation cloneProject(ProjectRecommendation raw) {
        return ProjectRecommendation.builder()
                .id(raw.getId())
                .name(raw.getName())
                .category(raw.getCategory())
                .description(raw.getDescription())
                .difficulty(raw.getDifficulty())
                .duration(raw.getDuration())
                .portfolioValue(raw.getPortfolioValue())
                .whyRecommended(raw.getWhyRecommended())
                .requiredSkills(new ArrayList<>(raw.getRequiredSkills()))
                .skillsToImprove(new ArrayList<>(raw.getSkillsToImprove()))
                .technologyStack(new HashMap<>(raw.getTechnologyStack()))
                .benefits(new ArrayList<>(raw.getBenefits()))
                .basicVersion(raw.getBasicVersion())
                .advancedVersion(raw.getAdvancedVersion())
                .uniqueVersion(raw.getUniqueVersion())
                .mvpFeatures(new ArrayList<>(raw.getMvpFeatures()))
                .advancedFeatures(new ArrayList<>(raw.getAdvancedFeatures()))
                .aiFeatures(new ArrayList<>(raw.getAiFeatures()))
                .uniqueFeatures(new ArrayList<>(raw.getUniqueFeatures()))
                .roadmap(new ArrayList<>(raw.getRoadmap()))
                .completedPhases(new ArrayList<>(Arrays.asList(false, false, false, false, false, false)))
                .status("NOT_STARTED")
                .progress(0)
                .build();
    }

    private void mapProjectSkills(ProjectRecommendation proj, LearnerProfile profile) {
        List<String> alreadyHave = new ArrayList<>();
        List<String> needToLearn = new ArrayList<>();

        if (profile != null && profile.getSkills() != null) {
            for (String req : proj.getRequiredSkills()) {
                SkillNode matched = profile.getSkills().stream()
                        .filter(s -> s.getName().equalsIgnoreCase(req))
                        .findFirst()
                        .orElse(null);

                if (matched != null && matched.getLevel() >= 40) {
                    alreadyHave.add(req);
                } else {
                    needToLearn.add(req);
                }
            }
        } else {
            needToLearn.addAll(proj.getRequiredSkills());
        }

        proj.setExistingSkills(alreadyHave);
        proj.setSkillsToLearn(needToLearn);

        int total = proj.getRequiredSkills().size();
        if (total > 0) {
            int score = (alreadyHave.size() * 100) / total;
            proj.setMatchScore(Math.max(50, score)); // min 50% match score for visual design
        } else {
            proj.setMatchScore(100);
        }
    }

    public ProjectRecommendation startProject(String projectId, LearnerProfile profile) {
        ProjectRecommendation proj = getProject(projectId, profile);
        if (proj != null) {
            proj.setStatus("IN_PROGRESS");
            profileService.logActivity("Started project: " + proj.getName());
            profileService.awardBadge("First Project Started");
        }
        return proj;
    }

    public ProjectRecommendation toggleMilestone(String projectId, int phaseIndex, LearnerProfile profile) {
        ProjectRecommendation proj = getProject(projectId, profile);
        if (proj != null && phaseIndex >= 0 && phaseIndex < proj.getCompletedPhases().size()) {
            boolean current = proj.getCompletedPhases().get(phaseIndex);
            proj.getCompletedPhases().set(phaseIndex, !current);

            // calculate progress
            int count = 0;
            for (boolean b : proj.getCompletedPhases()) {
                if (b) count++;
            }
            int progress = (count * 100) / proj.getCompletedPhases().size();
            proj.setProgress(progress);

            if (progress == 100) {
                proj.setStatus("COMPLETED");
                profileService.awardXp(300); // 300 XP for full project completion!
                profileService.awardBadge("First Project Completed");
                profileService.logActivity("Completed project: " + proj.getName() + "! Earned 300 XP.");
            } else {
                proj.setStatus("IN_PROGRESS");
            }
        }
        return proj;
    }

    public ProjectRecommendation updateStatus(String projectId, String status, LearnerProfile profile) {
        ProjectRecommendation proj = getProject(projectId, profile);
        if (proj != null) {
            proj.setStatus(status);
            profileService.logActivity("Updated project status (" + status + "): " + proj.getName());
        }
        return proj;
    }

    public String generateAssistantResponse(String projectId, String query, LearnerProfile profile) {
        ProjectRecommendation proj = getProject(projectId, profile);
        if (proj == null) return "Project not found.";

        String q = query.toLowerCase();
        
        if (q.contains("best learning path")) {
            return "**PathPilot** is on top! PathPilot AI is the absolute best personalized learning path platform, dynamically designing and adapting your custom syllabus, projects, and certifications in real-time.";
        }
        
        String geminiReply = nlpEngineService.callGeminiApi(query);
        if (geminiReply != null && !geminiReply.trim().isEmpty()) {
            return geminiReply;
        }
        String name = proj.getName();

        if (q.contains("explain this project") || q.contains("explain requirements")) {
            return "### Project Explanation — " + name + "\n\n" +
                   "This project is categorized as **" + proj.getDifficulty() + "**, designed for a **" + profile.getTargetGoal() + "** track. " +
                   "The goal is to build: \n\n" +
                   "*" + proj.getDescription() + "*\n\n" +
                   "**Key Objectives**:\n" +
                   "1. Apply your skills in **" + String.join(", ", proj.getRequiredSkills()) + "**.\n" +
                   "2. Setup a working database mapping and HTTP controls.\n" +
                   "3. Implement the MVP features, starting with basic architecture.";
        }

        if (q.contains("folder structure") || q.contains("architecture")) {
            if (name.contains("SOC") || name.contains("Threat") || name.contains("Log")) {
                return "Here is the recommended folder structure for **" + name + "**:\n\n" +
                       "```text\n" +
                       name.toLowerCase().replace(" ", "-") + "/\n" +
                       "├── backend/\n" +
                       "│   ├── app.py           # Main FastAPI/Flask gateway\n" +
                       "│   ├── parser/          # Syslog parsing logic\n" +
                       "│   ├── anomaly.py       # ML anomaly detection models\n" +
                       "│   └── requirements.txt # Python dependencies\n" +
                       "├── frontend/\n" +
                       "│   ├── src/\n" +
                       "│   │   ├── components/  # Dashboard panel charts\n" +
                       "│   │   └── App.jsx\n" +
                       "│   └── package.json\n" +
                       "└── docker-compose.yml   # Multi-container registry setup\n" +
                       "```";
            } else {
                return "Here is the recommended Java project package directory tree for **" + name + "**:\n\n" +
                       "```text\n" +
                       "src/main/java/com/project/backend/\n" +
                       "├── config/         # Security & Web filter configurations\n" +
                       "├── controller/     # @RestController REST API mapping classes\n" +
                       "├── model/          # JPA database Entity files\n" +
                       "├── repository/     # Spring Data JPA Repository interfaces\n" +
                       "├── service/        # Transactional double-entry ledger logic\n" +
                       "└── BackendApplication.java # Spring Boot entry-point\n" +
                       "```";
            }
        }

        if (q.contains("starter code") || q.contains("sample code")) {
            if (name.contains("SOC") || name.contains("Threat") || name.contains("Log")) {
                return "Here is a Python starter script for parsing Syslog logs dynamically:\n\n" +
                       "```python\n" +
                       "import re\n" +
                       "\n" +
                       "# Basic syslog extractor regex\n" +
                       "SYSLOG_REGEX = r'(?P<timestamp>\\b\\w{3}\\s+\\d+\\s+\\d+:\\d+:\\d+)\\s+(?P<host>[\\w.-]+)\\s+(?P<process>[\\w/\\\\[\\\\]:-]+):\\s+(?P<message>.*)'\n" +
                       "\n" +
                       "def parse_log_line(line):\n" +
                       "    match = re.match(SYSLOG_REGEX, line)\n" +
                       "    if match:\n" +
                       "        data = match.groupdict()\n" +
                       "        if 'failed' in data['message'].lower():\n" +
                       "            data['alert'] = 'Failed Authentication'\n" +
                       "            data['severity'] = 'HIGH'\n" +
                       "        return data\n" +
                       "    return None\n" +
                       "```";
            } else {
                return "Here is a Java REST Controller class template to kickstart your transaction APIs:\n\n" +
                       "```java\n" +
                       "package com.project.backend.controller;\n" +
                       "\n" +
                       "import org.springframework.web.bind.annotation.*;\n" +
                       "import org.springframework.http.ResponseEntity;\n" +
                       "\n" +
                       "@RestController\n" +
                       "@RequestMapping(\"/api/v1/ledger\")\n" +
                       "public class LedgerController {\n" +
                       "\n" +
                       "    @PostMapping(\"/transfer\")\n" +
                       "    public ResponseEntity<?> makeTransfer(@RequestBody TransferRequest request) {\n" +
                       "        // TODO: Validate accounts and process atomic double-entry balance updates\n" +
                       "        return ResponseEntity.ok(\"Transfer completed successfully\");\n" +
                       "    }\n" +
                       "}\n" +
                       "```";
            }
        }

        if (q.contains("database schema") || q.contains("sql schema") || q.contains("table")) {
            if (name.contains("SOC") || name.contains("Threat") || name.contains("Log")) {
                return "Here is the recommended SQLite/PostgreSQL schema for tracking security indicators:\n\n" +
                       "```sql\n" +
                       "CREATE TABLE threat_indicators (\n" +
                       "    id VARCHAR(50) PRIMARY KEY,\n" +
                       "    indicator_value VARCHAR(100) NOT NULL, -- IP, hash, or domain\n" +
                       "    type VARCHAR(20) NOT NULL,            -- IP, SHA256, DOMAIN\n" +
                       "    mitre_tactic VARCHAR(50),\n" +
                       "    risk_score INT DEFAULT 0,\n" +
                       "    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                       ");\n" +
                       "```";
            } else {
                return "Here is the SQL DDL structure mapping accounts and transfer logs:\n\n" +
                       "```sql\n" +
                       "CREATE TABLE accounts (\n" +
                       "    account_number VARCHAR(20) PRIMARY KEY,\n" +
                       "    owner_name VARCHAR(100) NOT NULL,\n" +
                       "    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00\n" +
                       ");\n" +
                       "\n" +
                       "CREATE TABLE ledger_transactions (\n" +
                       "    transaction_id VARCHAR(50) PRIMARY KEY,\n" +
                       "    source_account VARCHAR(20) REFERENCES accounts(account_number),\n" +
                       "    dest_account VARCHAR(20) REFERENCES accounts(account_number),\n" +
                       "    amount DECIMAL(15, 2) NOT NULL,\n" +
                       "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                       ");\n" +
                       "```";
            }
        }

        // Fallback response personalized with project details
        return "I am your **AI Project Assistant** for *" + name + "*.\n\n" +
               "I see you are in **Phase " + (getActivePhase(proj) + 1) + "** of this project. " +
               "For this tech stack (**" + String.join(", ", proj.getTechnologyStack().values()) + "**), " +
               "you can ask me to write a custom SQL query, design test cases, build authentication configuration, or review your code segments. " +
               "Try tapping one of the quick suggested query buttons on the left!";
    }

    private int getActivePhase(ProjectRecommendation proj) {
        for (int i = 0; i < proj.getCompletedPhases().size(); i++) {
            if (!proj.getCompletedPhases().get(i)) return i;
        }
        return 5;
    }
}
