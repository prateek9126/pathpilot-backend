package com.example.backend.service;

import com.example.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificationService {

    @Autowired
    private ProfileService profileService;

    private final List<CertificationRecommendation> certDb = new ArrayList<>();
    private final Map<String, CertificationRecommendation> activeCerts = new HashMap<>();

    public CertificationService() {
        seedCertifications();
    }

    private void seedCertifications() {
        // ==================== CYBERSECURITY / SOC ANALYST ====================
        certDb.add(CertificationRecommendation.builder()
                .id("cert_soc_1")
                .name("Google Cybersecurity Professional Certificate")
                .provider("Coursera / Google")
                .category("Cybersecurity")
                .description("Validate entry-level cybersecurity skills. Cover security operations, Python scripting, Linux commands, SQL queries, and SIEM tools.")
                .difficulty("Beginner")
                .price(3200)
                .isFree(false)
                .duration("3 months")
                .validity("Lifetime")
                .examFormat("Hands-on Quizzes & Capstone Project")
                .requiredSkills(Arrays.asList("Python", "Linux", "SIEM", "Threat Detection"))
                .careerRelevance("High")
                .industryRecognition("High")
                .portfolioValue("High")
                .benefits(Arrays.asList("Prepares you for entry-level SOC roles", "Provides direct shareable badge for LinkedIn", "Endorsed by major security employers"))
                .preparationRoadmap(Arrays.asList(
                        "Phase 1 - Fundamentals: Learn foundation security assets and network ports.",
                        "Phase 2 - Technical Skills: Practice Linux cmd controls and basic SQL filters.",
                        "Phase 3 - Log Analysis: Load logs in Splunk/Chronicle and query security incidents.",
                        "Phase 4 - Scripting: Automate parsing scripts in Python.",
                        "Phase 5 - Capstone: Complete final breach incident case study."
                ))
                .build());

        certDb.add(CertificationRecommendation.builder()
                .id("cert_soc_2")
                .name("CompTIA Security+")
                .provider("CompTIA")
                .category("Cybersecurity")
                .description("The global standard for validating baseline security skills. Map threats, configure network defenses, and understand risk compliance.")
                .difficulty("Intermediate")
                .price(29500)
                .isFree(false)
                .duration("45 hours")
                .validity("3 Years")
                .examFormat("Multiple Choice (MCQ) & Performance-Based Questions")
                .requiredSkills(Arrays.asList("Networking", "Threat Detection", "Log Analysis", "Incident Response"))
                .careerRelevance("Very High")
                .industryRecognition("Very High")
                .portfolioValue("High")
                .benefits(Arrays.asList("Highly demanded in corporate cybersecurity recruiting", "Satisfies DoD 8570 compliance metrics", "Covers fundamental cryptographic protocols"))
                .preparationRoadmap(Arrays.asList(
                        "Phase 1 - Threat Landscape: Identify virus types, social engineering, and protocol vulnerabilities.",
                        "Phase 2 - Architecture: Design secure zones and firewall rules.",
                        "Phase 3 - Implementation: Configure WPA3, TLS, and token authorization.",
                        "Phase 4 - Incident Management: Analyze syslog events and audit compliance policies.",
                        "Phase 5 - Final Review: Run full length simulation tests."
                ))
                .build());

        certDb.add(CertificationRecommendation.builder()
                .id("cert_soc_3")
                .name("Fortinet Certified Associate in Cybersecurity")
                .provider("Fortinet")
                .category("Cybersecurity")
                .description("A completely free entry-level credential validating knowledge of firewall operations, secure gateways, and network threat structures.")
                .difficulty("Beginner")
                .price(0)
                .isFree(true)
                .duration("15 hours")
                .validity("2 Years")
                .examFormat("Multiple Choice Questions (MCQ)")
                .requiredSkills(Arrays.asList("Networking", "Threat Detection"))
                .careerRelevance("Medium")
                .industryRecognition("Medium")
                .portfolioValue("Medium")
                .benefits(Arrays.asList("Genuinely free training and free official certificate exam", "Covers real-world FortiOS gateway protocols", "Ideal for networking starters"))
                .preparationRoadmap(Arrays.asList(
                        "Phase 1 - Gateway Basics: Learn network threats and secure web gateway concepts.",
                        "Phase 2 - Firewall Policies: Explore FortiGate configuration patterns.",
                        "Phase 3 - Log Audit: Track threat logs on FortiAnalyzer.",
                        "Phase 4 - Certification: Pass Fortinet official certification assessment."
                ))
                .build());

        certDb.add(CertificationRecommendation.builder()
                .id("cert_soc_4")
                .name("Certified Information Systems Security Professional (CISSP)")
                .provider("ISC2")
                .category("Cybersecurity")
                .requiredSkills(Arrays.asList("Threat Detection", "Incident Response", "Log Analysis"))
                .difficulty("Advanced").price(58000).duration("6 months").validity("3 Years").examFormat("CAT MCQ").build());
        certDb.add(CertificationRecommendation.builder()
                .id("cert_soc_5")
                .name("eLearnSecurity Certified Junior Penetration Tester (eJPT)")
                .provider("INE")
                .category("Cybersecurity")
                .requiredSkills(Arrays.asList("Python", "Linux", "Networking"))
                .difficulty("Intermediate").price(16500).duration("1 month").validity("Lifetime").examFormat("Hands-on Lab").build());
        certDb.add(CertificationRecommendation.builder()
                .id("cert_soc_6")
                .name("Microsoft Certified: Security Fundamentals (SC-900)")
                .provider("Microsoft")
                .category("Cybersecurity")
                .requiredSkills(Arrays.asList("Networking", "Threat Detection"))
                .difficulty("Beginner").price(3696).duration("10 hours").validity("Lifetime").examFormat("MCQ").build());

        // ==================== JAVA BACKEND ====================
        certDb.add(CertificationRecommendation.builder()
                .id("cert_java_1")
                .name("Oracle Certified Professional: Java SE 17 Developer")
                .provider("Oracle")
                .category("Software Development")
                .description("The official advanced certification validating standard Java programming capabilities, modular architecture, concurrency, and OOP syntax.")
                .difficulty("Advanced")
                .price(21000)
                .isFree(false)
                .duration("60 hours")
                .validity("Lifetime")
                .examFormat("Multiple Choice (MCQ)")
                .requiredSkills(Arrays.asList("Java", "OOP", "SQL"))
                .careerRelevance("High")
                .industryRecognition("Very High")
                .portfolioValue("High")
                .benefits(Arrays.asList("Verifies mastery of core language mechanics", "Official credential recognized by enterprise employers globally", "Covers JDK 17 modern features"))
                .preparationRoadmap(Arrays.asList(
                        "Phase 1 - Class Design: Refine nested classes, enums, record types, and sealed classes.",
                        "Phase 2 - Core APIs: Study Collections Framework, streams, lambdas, and functional interfaces.",
                        "Phase 3 - Concurrency: Manage threads, executors, and atomic lock variables.",
                        "Phase 4 - IO & Database: Understand JDBC transactions, serializations, and NIO.2.",
                        "Phase 5 - Exam Practice: Solve complex mock questions."
                ))
                .build());

        certDb.add(CertificationRecommendation.builder()
                .id("cert_java_2")
                .name("AWS Certified Developer - Associate")
                .provider("Amazon Web Services")
                .category("Cloud")
                .description("Validate skills in deploying and maintaining Spring Boot and microservices architectures on AWS infrastructure.")
                .difficulty("Intermediate")
                .price(12500)
                .isFree(false)
                .duration("40 hours")
                .validity("3 Years")
                .examFormat("Multiple Choice (MCQ)")
                .requiredSkills(Arrays.asList("Java", "Spring Boot", "REST APIs"))
                .careerRelevance("Very High")
                .industryRecognition("Very High")
                .portfolioValue("Very High")
                .benefits(Arrays.asList("Verifies cloud deployment competence", "Highly valued for full stack developer roles", "Teaches serverless architectures (Lambda, DynamoDB)"))
                .preparationRoadmap(Arrays.asList(
                        "Phase 1 - AWS Basics: Learn IAM users, VPC configurations, and EC2 deployment.",
                        "Phase 2 - Serverless Development: Configure Lambda endpoints and DynamoDB integrations.",
                        "Phase 3 - Security: Manage KMS decryption and Cognito user login filters.",
                        "Phase 4 - Deployment CI/CD: Construct CodePipeline scripts for automatic pushes.",
                        "Phase 5 - Practice Exam: Pass 3 full prep tests."
                ))
                .build());

        certDb.add(CertificationRecommendation.builder()
                .id("cert_java_3")
                .name("Hackerrank Java Programming Certificate")
                .provider("Hackerrank")
                .category("Software Development")
                .description("A completely free coding skills test verifying knowledge of Java collections, basic object-oriented constructs, and algorithms.")
                .difficulty("Beginner")
                .price(0)
                .isFree(true)
                .duration("3 hours")
                .validity("Lifetime")
                .examFormat("Interactive Coding Assessments")
                .requiredSkills(Arrays.asList("Java", "OOP"))
                .careerRelevance("Medium")
                .industryRecognition("Medium")
                .portfolioValue("Medium")
                .benefits(Arrays.asList("Genuinely free certificate validation", "Tests pure programming problem-solving", "Perfect indicator for junior developer resumes"))
                .preparationRoadmap(Arrays.asList(
                        "Phase 1 - Core Java: Review exceptions, loops, and list sorting.",
                        "Phase 2 - Algorithms: Practice array structures and hashing.",
                        "Phase 3 - Certification: Complete 2-hour coding test."
                ))
                .build());

        certDb.add(CertificationRecommendation.builder()
                .id("cert_java_4")
                .name("Spring Certified Professional")
                .provider("Broadcom / VMware")
                .category("Software Development")
                .requiredSkills(Arrays.asList("Java", "Spring Boot", "REST APIs", "JPA / Hibernate"))
                .difficulty("Advanced").price(21000).duration("3 months").validity("Lifetime").examFormat("MCQ").build());
        certDb.add(CertificationRecommendation.builder()
                .id("cert_java_5")
                .name("Meta Back-End Developer Professional Certificate")
                .provider("Coursera / Meta")
                .category("Software Development")
                .requiredSkills(Arrays.asList("Java", "OOP", "SQL"))
                .difficulty("Beginner").price(3200).duration("4 months").validity("Lifetime").examFormat("Practical Lab").build());
        certDb.add(CertificationRecommendation.builder()
                .id("cert_java_6")
                .name("Oracle Certified Associate, Java SE 8 Programmer")
                .provider("Oracle")
                .category("Software Development")
                .requiredSkills(Arrays.asList("Java", "OOP"))
                .difficulty("Intermediate").price(18000).duration("40 hours").validity("Lifetime").examFormat("MCQ").build());


        // ==================== DATA SCIENCE ====================
        certDb.add(CertificationRecommendation.builder()
                .id("cert_ds_1")
                .name("Google Data Analytics Professional Certificate")
                .provider("Coursera / Google")
                .category("Data Science")
                .description("Validate foundational skills in data manipulation, cleaning, visualization, and programming in R/Python.")
                .difficulty("Beginner")
                .price(3200)
                .isFree(false)
                .duration("4 months")
                .validity("Lifetime")
                .examFormat("Quizzes, case studies, and practical projects")
                .requiredSkills(Arrays.asList("Python", "Statistics", "Data Visualization"))
                .careerRelevance("High")
                .industryRecognition("High")
                .portfolioValue("High")
                .benefits(Arrays.asList("Great starting point for data analysts", "Direct shareable credential badge", "Covers SQL, spreadsheets, and R/Python visualization"))
                .preparationRoadmap(Arrays.asList(
                        "Phase 1 - Data Concepts: Understand cleaning and metrics.",
                        "Phase 2 - SQL Analytics: Write query filters and joins.",
                        "Phase 3 - Visualization: Build dashboards in Tableau.",
                        "Phase 4 - Programming: Parse datasets using R/Python.",
                        "Phase 5 - Capstone: Complete consumer trend case study."
                ))
                .build());

        certDb.add(CertificationRecommendation.builder()
                .id("cert_ds_2")
                .name("TensorFlow Developer Certificate")
                .provider("TensorFlow")
                .category("AI & Machine Learning")
                .description("Validate expertise in building deep learning neural networks, natural language models, and computer vision classifiers using TensorFlow.")
                .difficulty("Advanced")
                .price(8500)
                .isFree(false)
                .duration("2 months")
                .validity("Lifetime")
                .examFormat("Practical Coding (Exam runs inside IDE)")
                .requiredSkills(Arrays.asList("Python", "Machine Learning", "Data Visualization"))
                .careerRelevance("Very High")
                .industryRecognition("Very High")
                .portfolioValue("Very High")
                .benefits(Arrays.asList("Demonstrates deep learning capabilities", "Listed on the official TensorFlow certificate network", "Tests actual coding performance under load"))
                .preparationRoadmap(Arrays.asList(
                        "Phase 1 - Neural Networks: Train basic dense layers in TF.",
                        "Phase 2 - Computer Vision: Write convolutional neural networks (CNNs).",
                        "Phase 3 - Natural Language Processing: Implement LSTMs and tokenizers.",
                        "Phase 4 - Time Series: Predict values using GRU networks.",
                        "Phase 5 - Practice: Run local IDE tests."
                ))
                .build());

        certDb.add(CertificationRecommendation.builder()
                .id("cert_ds_3")
                .name("Kaggle Machine Learning Certification")
                .provider("Kaggle")
                .category("AI & Machine Learning")
                .description("A completely free interactive coding certification proving competency in data preprocessing, feature engineering, and training Scikit-Learn classifiers.")
                .difficulty("Beginner")
                .price(0)
                .isFree(true)
                .duration("10 hours")
                .validity("Lifetime")
                .examFormat("Interactive Code Completion Tests")
                .requiredSkills(Arrays.asList("Python", "Machine Learning"))
                .careerRelevance("Medium")
                .industryRecognition("Medium")
                .portfolioValue("Medium")
                .benefits(Arrays.asList("Genuinely free training and certificate", "Excellent hands-on notebook workspace", "Strong validation of core ML functions"))
                .preparationRoadmap(Arrays.asList(
                        "Phase 1 - Basic ML: Build Random Forest regression pipelines.",
                        "Phase 2 - Model Tuning: Optimize scoring metrics and cross-validate.",
                        "Phase 3 - Certification: Complete intermediate Kaggle workbook."
                ))
                .build());

        certDb.add(CertificationRecommendation.builder()
                .id("cert_ds_4")
                .name("Microsoft Certified: Azure Data Scientist (DP-100)")
                .provider("Microsoft")
                .category("Cloud")
                .requiredSkills(Arrays.asList("Python", "Statistics", "Machine Learning"))
                .difficulty("Intermediate").price(4800).duration("30 hours").validity("1 Year").examFormat("MCQ").build());
        certDb.add(CertificationRecommendation.builder()
                .id("cert_ds_5")
                .name("IBM Data Science Professional Certificate")
                .provider("Coursera / IBM")
                .requiredSkills(Arrays.asList("Python", "Statistics", "Data Visualization"))
                .category("Data Science").difficulty("Beginner").price(3200).duration("3 months").validity("Lifetime").examFormat("Practical Lab").build());
        certDb.add(CertificationRecommendation.builder()
                .id("cert_ds_6")
                .name("Databricks Spark Developer Certification")
                .provider("Databricks")
                .requiredSkills(Arrays.asList("Python", "Machine Learning"))
                .category("Data Science").difficulty("Intermediate").price(16500).duration("40 hours").validity("2 Years").examFormat("MCQ").build());
    }

    public List<CertificationRecommendation> getRecommendations(LearnerProfile profile, int maxBudget, String categoryFilter) {
        String goal = profile.getTargetGoal().toLowerCase();
        String primaryTrack = "Cybersecurity";
        if (goal.contains("java") || goal.contains("backend") || goal.contains("spring")) {
            primaryTrack = "Software Development";
        } else if (goal.contains("data") || goal.contains("scientist") || goal.contains("machine") || goal.contains("ds")) {
            primaryTrack = "Data Science";
        }

        final String activeTrack = primaryTrack;

        List<CertificationRecommendation> list = certDb.stream()
                .filter(c -> {
                    // Maximum budget filter
                    if (c.getPrice() > maxBudget) return false;
                    
                    // Category filter logic
                    if (categoryFilter != null && !"All".equalsIgnoreCase(categoryFilter)) {
                        return c.getCategory().equalsIgnoreCase(categoryFilter);
                    }
                    
                    // Default list highlights career-related certificates
                    return c.getCategory().equalsIgnoreCase(activeTrack) || c.getCategory().equalsIgnoreCase("Cloud") || c.getCategory().equalsIgnoreCase("AI & Machine Learning");
                })
                .collect(Collectors.toList());

        // Process dynamic fields
        for (CertificationRecommendation cert : list) {
            mapCertSkills(cert, profile);
            determineWhyRecommended(cert, profile);
        }

        // Sort by match score
        list.sort((c1, c2) -> Integer.compare(c2.getMatchScore(), c1.getMatchScore()));

        return list;
    }

    public CertificationRecommendation getCertDetails(String certId, LearnerProfile profile) {
        CertificationRecommendation cert = activeCerts.get(certId);
        if (cert == null) {
            CertificationRecommendation raw = certDb.stream()
                    .filter(c -> c.getId().equals(certId))
                    .findFirst()
                    .orElse(null);
            
            if (raw == null) return null;
            
            cert = cloneCert(raw);
            mapCertSkills(cert, profile);
            determineWhyRecommended(cert, profile);
            activeCerts.put(certId, cert);
        } else {
            mapCertSkills(cert, profile);
            determineWhyRecommended(cert, profile);
        }
        return cert;
    }

    private CertificationRecommendation cloneCert(CertificationRecommendation raw) {
        return CertificationRecommendation.builder()
                .id(raw.getId())
                .name(raw.getName())
                .provider(raw.getProvider())
                .description(raw.getDescription())
                .category(raw.getCategory())
                .difficulty(raw.getDifficulty())
                .price(raw.getPrice())
                .isFree(raw.isFree())
                .duration(raw.getDuration())
                .validity(raw.getValidity())
                .examFormat(raw.getExamFormat())
                .careerRelevance(raw.getCareerRelevance())
                .industryRecognition(raw.getIndustryRecognition())
                .portfolioValue(raw.getPortfolioValue())
                .requiredSkills(new ArrayList<>(raw.getRequiredSkills()))
                .benefits(new ArrayList<>(raw.getBenefits()))
                .preparationRoadmap(new ArrayList<>(raw.getPreparationRoadmap()))
                .completedPhases(new ArrayList<>(Arrays.asList(false, false, false, false, false)))
                .status("INTERESTED")
                .progress(0)
                .saved(false)
                .build();
    }

    private void mapCertSkills(CertificationRecommendation cert, LearnerProfile profile) {
        List<String> alreadyHave = new ArrayList<>();
        List<String> needToLearn = new ArrayList<>();

        if (profile != null && profile.getSkills() != null) {
            for (String req : cert.getRequiredSkills()) {
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
            needToLearn.addAll(cert.getRequiredSkills());
        }

        cert.setExistingSkills(alreadyHave);
        cert.setSkillsToLearn(needToLearn);

        int total = cert.getRequiredSkills().size();
        if (total > 0) {
            int score = (alreadyHave.size() * 100) / total;
            cert.setMatchScore(Math.max(50, score)); // default min 50%
        } else {
            cert.setMatchScore(100);
        }
    }

    private void determineWhyRecommended(CertificationRecommendation cert, LearnerProfile profile) {
        if (profile == null) {
            cert.setWhyRecommended("Recommended to build certifications context in your target career.");
            return;
        }

        String goal = profile.getTargetGoal();
        
        // 1. Completed projects trigger
        boolean projectMatch = false;
        if (profile.getPreviousProjects() != null) {
            for (String proj : profile.getPreviousProjects()) {
                String pLower = proj.toLowerCase();
                if (pLower.contains("soc") || pLower.contains("monitoring") || pLower.contains("threat")) {
                    if (cert.getCategory().equalsIgnoreCase("Cybersecurity")) {
                        cert.setWhyRecommended("Based on your '" + proj + "' project, this certification complements your portfolio credentials and validates your practical threat modeling experience.");
                        projectMatch = true;
                        break;
                    }
                }
                if (pLower.contains("banking") || pLower.contains("ledger") || pLower.contains("commerce")) {
                    if (cert.getCategory().equalsIgnoreCase("Software Development") || cert.getCategory().equalsIgnoreCase("Cloud")) {
                        cert.setWhyRecommended("Based on your '" + proj + "' project, this certification validates the serverless cloud deployment and backend configurations of your app.");
                        projectMatch = true;
                        break;
                    }
                }
            }
        }

        if (projectMatch) return;

        // 2. Skill gaps triggers
        if (cert.getSkillsToLearn().size() > 0) {
            cert.setWhyRecommended("You are targeting a " + goal + " track and currently need " + String.join(", ", cert.getSkillsToLearn()) + " skills. Preparing for this certification aligns with filling those crucial gaps.");
        } else {
            cert.setWhyRecommended("Highly recommended because you already validate 100% of the foundational requirements (including " + String.join(", ", cert.getExistingSkills()) + ") and can fast-track the exam.");
        }
    }

    public CertificationRecommendation saveCertification(String id, boolean saveState) {
        // Find in DB
        CertificationRecommendation cert = certDb.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
        
        if (cert != null) {
            cert.setSaved(saveState);
            if (saveState) cert.setStatus("SAVED");
            else cert.setStatus("INTERESTED");
        }
        return cert;
    }

    public CertificationRecommendation updateStatus(String id, String status) {
        // Load details first
        LearnerProfile profile = profileService.getProfile();
        CertificationRecommendation cert = getCertDetails(id, profile);
        if (cert != null) {
            cert.setStatus(status);
            if ("COMPLETED".equals(status)) {
                cert.setProgress(100);
                profileService.awardXp(500); // 500 XP for certification validation!
                profileService.logActivity("Earned Certification: " + cert.getName() + " by " + cert.getProvider() + "! +500 XP.");
            }
            if ("PREPARING".equals(status)) {
                profileService.logActivity("Started preparing for exam: " + cert.getName());
            }
        }
        return cert;
    }

    public CertificationRecommendation toggleMilestone(String id, int phaseIndex) {
        LearnerProfile profile = profileService.getProfile();
        CertificationRecommendation cert = getCertDetails(id, profile);
        if (cert != null && phaseIndex >= 0 && phaseIndex < cert.getCompletedPhases().size()) {
            boolean current = cert.getCompletedPhases().get(phaseIndex);
            cert.getCompletedPhases().set(phaseIndex, !current);

            // calculate progress
            int count = 0;
            for (boolean b : cert.getCompletedPhases()) {
                if (b) count++;
            }
            int progress = (count * 100) / cert.getCompletedPhases().size();
            cert.setProgress(progress);

            if (progress == 100) {
                cert.setStatus("COMPLETED");
                profileService.awardXp(500);
                profileService.logActivity("Passed Certification Exam: " + cert.getName() + "! Earned 500 XP.");
            } else {
                cert.setStatus("PREPARING");
            }
        }
        return cert;
    }

    public String generateAssistantResponse(String certId, String query) {
        LearnerProfile profile = profileService.getProfile();
        CertificationRecommendation cert = getCertDetails(certId, profile);
        if (cert == null) return "Certification not found.";

        String q = query.toLowerCase();
        String name = cert.getName();

        if (q.contains("30-day study plan") || q.contains("study plan")) {
            return "### 📅 30-Day Certification Study Plan — " + name + "\n\n" +
                   "Here is a structured preparation curriculum to ace the exam:\n\n" +
                   "* **Days 1–10: Domain Fundamentals**\n" +
                   "  - Target core topics: **" + String.join(", ", cert.getRequiredSkills()) + "**.\n" +
                   "  - Complete recommended study documentation.\n" +
                   "* **Days 11–20: Core Practice Labs**\n" +
                   "  - Practice on interactive terminal scripts (Linux/AWS configuration).\n" +
                   "  - Tackle mock quizzes on intermediate scenarios.\n" +
                   "* **Days 21–27: Practice Exams**\n" +
                   "  - Take 3 full-length mock exams.\n" +
                   "  - Audit weak domains and revise incorrect answers.\n" +
                   "* **Days 28–30: Exam Registration**\n" +
                   "  - Complete final mock validation (aiming for >85%).\n" +
                   "  - Schedule and take the official certification exam!";
        }

        if (q.contains("test me") || q.contains("generate quiz") || q.contains("practice question")) {
            if (name.contains("Security") || name.contains("Cybersecurity")) {
                return "### 📝 Practice Exam Question — " + name + "\n\n" +
                       "**Question**: An audit log displays a repeating sequence of failed authentication attempts targeting port 22, originating from an external IP address, followed by a successful login. What is the most immediate action the SOC analyst should perform?\n\n" +
                       "A) Add the IP address to a blocklist firewall policy.\n" +
                       "B) Reset the target account's credentials and quarantine the host to investigate lateral movement.\n" +
                       "C) Ignore, as the final login was successful.\n" +
                       "D) Disable port 22 globally.\n\n" +
                       "**Correct Answer**: **B**\n" +
                       "**Explanation**: Port 22 corresponds to SSH. A sequence of failed logins followed by success represents a potential brute-force intrusion. Credential recycling and host quarantine are required immediately.";
            } else {
                return "### 📝 Practice Exam Question — " + name + "\n\n" +
                       "**Question**: Which Spring annotation is used to ensure that a method executes within a database transaction boundary, rolling back modifications automatically if a runtime exception is thrown?\n\n" +
                       "A) `@RestController`\n" +
                       "B) `@Transactional`\n" +
                       "C) `@Autowired`\n" +
                       "D) `@PersistenceContext`\n\n" +
                       "**Correct Answer**: **B**\n" +
                       "**Explanation**: `@Transactional` ensures JDBC database actions execute atomically with ACID integrity, triggering rollbacks on runtime exceptions.";
            }
        }

        if (q.contains("explain my weak") || q.contains("what should i study")) {
            if (cert.getSkillsToLearn().size() > 0) {
                return "According to your profile, you should focus on: **" + String.join(", ", cert.getSkillsToLearn()) + "**.\n\n" +
                       "I suggest: \n" +
                       "1. Navigate to the **Learning Resources** page.\n" +
                       "2. Filter for tutorials on these topics within your budget.\n" +
                       "3. Complete the assessment quizzes inside the Workspace panel to validate your skills!";
            } else {
                return "You have validated all required core skills for this certification! I recommend moving straight to **Mock Exams** and booking the test.";
            }
        }

        return "I am your **AI Certification Prep Assistant** for *" + name + "*.\n\n" +
               "I can design a study plan, quiz you on core skills (**" + String.join(", ", cert.getRequiredSkills()) + "**), or explain correct mock exam answers. " +
               "Try tapping one of the quick study chips below!";
    }
}
