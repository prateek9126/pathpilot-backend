# PathPilot Backend ⚙️

This is the Spring Boot REST API backend for the **PathPilot AI** platform. It is built using Java 21, Spring Boot 3.3.3, and Maven.

---

## 📂 Codebase Structure

The backend application code is organized as follows:

```text
pathpilot-backend/
├── src/
│   ├── main/
│   │   ├── java/com/example/backend/
│   │   │   ├── controller/
│   │   │   │   └── ApiController.java             # REST controller exposing endpoints
│   │   │   ├── model/
│   │   │   │   ├── LearnerProfile.java            # Onboarding & progress tracker
│   │   │   │   ├── RoadmapPhase.java              # Phases of learning paths
│   │   │   │   ├── RoadmapModule.java             # Active modules & quiz details
│   │   │   │   ├── LearningResource.java          # Courses, videos, articles recommendations
│   │   │   │   ├── ProjectRecommendation.java     # Hands-on projects details
│   │   │   │   ├── CertificationRecommendation.java # Certs mapping & prep study plans
│   │   │   │   ├── CareerPath.java                # Careers demand, salaries & transitions
│   │   │   │   ├── ChatMessage.java               # Chat message logs
│   │   │   │   ├── Resource.java                  # Helper generic resource model
│   │   │   │   └── SkillNode.java                 # Skill demand flow chart nodes
│   │   │   ├── service/
│   │   │   │   ├── ProfileService.java            # Manages current active profile state
│   │   │   │   ├── DemoService.java               # Configures preloaded demo track profiles
│   │   │   │   ├── NlpEngineService.java          # Parses natural language prompts
│   │   │   │   ├── RoadmapService.java            # Manages adaptive phase completions
│   │   │   │   ├── RecommendationService.java     # Generates courses based on budget
│   │   │   │   ├── ProjectService.java            # Compares and details project builds
│   │   │   │   ├── CertificationService.java      # Suggests certs & generates prep guides
│   │   │   │   ├── CareerService.java             # Powers trajectory transitions & charts
│   │   │   │   └── StateService.java              # Stores active user state in memory
│   │   │   └── BackendApplication.java            # Main entry point
│   │   └── resources/
│   │       ├── application.properties             # App configuration (port, profiles)
│   │       ├── static/                            # Static asset mappings
│   │       └── templates/                         # HTML view templates
│   └── test/
│       └── java/com/example/backend/
│           ├── BackendApplicationTests.java       # Boot context loader tests
│           └── NlpAndRoadmapTests.java            # Tests checking NLP engine & roadmaps
├── pom.xml                                        # Maven project dependencies file
└── mvnw.cmd                                       # Maven wrapper executable script
```

---

## 🔌 API Endpoints Mapping

The REST APIs are exposed under `/api` in `ApiController.java`. CORS is enabled globally via `@CrossOrigin(origins = "*", allowedHeaders = "*")` to support developer client workspaces.

### 👤 Profile & Onboarding APIs
- `GET /api/profile` - Fetches the current active user profile.
- `POST /api/onboard` - Accepts natural language or a custom profile to build a learning track.
- `PUT /api/profile` - Manages updates to user goals, focus skills, and budget.
- `POST /api/demo/{demoId}` - Configures preloaded profiles (`frontend`, `cloud`, `cyber`, `ml`).

### 🧭 Roadmap & Workspace APIs
- `GET /api/roadmap` - Fetches current customized learning roadmap.
- `GET /api/roadmap/modules/{moduleId}` - Returns module details.
- `POST /api/roadmap/modules/{moduleId}/complete` - Completes modules that don't require quizzes.
- `POST /api/roadmap/modules/{moduleId}/assessment` - Grades MCQ quizzes and updates dashboard scores.
- `POST /api/roadmap/modules/{moduleId}/feedback` - Submits difficulty ratings to dynamically tweak future phases.

### 🎓 Learning Hub APIs
- `GET /api/recommendations` - Returns filtered courses/learning resources.
- `GET /api/projects` - Returns 6 hands-on portfolio projects.
- `GET /api/projects/{projectId}` - Prepares prerequisite audits and feature checklists.
- `POST /api/projects/{projectId}/start` - Sets project status to active.
- `POST /api/projects/compare` - Returns side-by-side matrices comparing selected project IDs.
- `POST /api/projects/{projectId}/assistant` - Interacts with the AI Project Assistant for code blueprints.
- `GET /api/certifications` - Matches credentials based on budget and category filters.
- `POST /api/certifications/{certId}/save` - Adds certification to favorites.
- `POST /api/certifications/{certId}/assistant` - Generates 30-day preparation calendars.

### 📈 Career overview APIs
- `GET /api/career/overview` - Pulls career trajectories and demand scores.
- `GET /api/career/transition` - Calculates transitional pathways ("What If I Switch?").
- `GET /api/career/companies` - Gathers job listings matching goals.
- `POST /api/career/advisor` - Triggers career advisor chat responses.

---

## 🛠️ CLI Development Commands

Run these standard Maven commands from the `pathpilot-backend` directory:

```bash
# Compile and fetch dependencies
./mvnw.cmd compile

# Execute test suite (verifies controllers, services and parsing algorithms)
./mvnw.cmd test

# Run the backend locally
./mvnw.cmd spring-boot:run
```
