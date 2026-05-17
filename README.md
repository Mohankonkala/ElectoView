# ElectroView - Electricity Usage Analytics Dashboard

ElectroView is a real-time data analytics dashboard application for monitoring and analysing electricity consumption across residential and municipal infrastructure. The system takes in meter readings, visualises usage patterns, detects anomalies, and generates reports to support informed decisions by utility providers and end-users.

Once completed, ElectroView will provide:
- Live electricity consumption monitoring per zone/household
- Historical trend analysis and forecasting
- Anomaly and fault detection alerts
- Usage reports exportable for billing and auditing
- Role-based access for administrators, analysts, and consumers

---

## Project Documents

[Specification](./Specification.md) Full system specification including domain, problem statement, scope, functional and non-functional requirements, and use cases.

[Architecture](./Architecture.md) C4 architectural diagrams (Context, Container, Component, Code) with Mermaid source.

[Stakeholders](./Stakeholders.md) Stakeholder analysis: 7 stakeholders with roles, key concerns, pain points, success metrics, and conflict mapping.

[System Requirements Document](./SRD.md) System Requirements Document: 12 functional requirements with acceptance criteria + 14 non-functional requirements across 6 quality categories.

[Use Cases](./UseCases.md) UML use case diagram (Mermaid), actor descriptions, 8 detailed use case specifications with flows and alternative flows.

[Test Cases](./TestCases.md) Functional test cases + non-functional test cases.

[Agile Planning](./Agile_Planning.md) 12 user stories (INVEST-compliant), MoSCoW-prioritised product backlog, Sprint 1 plan with 22 tasks.

[Reflection](./Reflection.md) Reflection on challenges faced in the documentation of the project.

[Template Analysis](./template_analysis.md) Comparison of 4 GitHub project templates with justification for selecting Team Planning.

[Kanban Explanation](./kanban_explanation.md) Kanban board definition, 7-column design rationale, WIP limits, and README section for the board.

[State Diagrams](./State_Diagrams.md) State transition diagrams for 8 objects: User Account, Meter Reading, Anomaly, Consumption Report, Zone, Smart Meter, Consumer Budget, User Session.

[Activity Diagrams](./Activity_Diagram.md) Activity diagrams for 8 workflows: Login, Meter Ingestion, Anomaly Resolution, Consumer Dashboard, Report Export, Threshold Config, User Management, Executive KPI.

[Domain Model](./Domain_Model.md) 7 domain entities with attributes, methods, relationships, and 10 business rules.

[Class Diagram](./Class_Diagram.md) Full Mermaid.js class diagram with enumerations, 8 domain classes, 3 service boundary classes, multiplicity, composition, and design decision explanations.

[Repository Class Diagram](./Repository_Class_Diagram.md) This diagram shows the persistence repository layer. It demonstrates the Repository pattern with multiple swappable storage backends, accessed through a Factory abstraction mechanism.

[OpenAPI/Swagger file](./Swagger.md) Interactive API documentation is available via Swagger UI when the application is running: http://localhost:8080/swagger-ui.html

---

## ElectroView Project

### Language and Framework

| Item | Choice |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.3 |
| Database | MySQL (production) |
| ORM | Spring Data JPA / Hibernate |
| Boilerplate reduction | Lombok |
| Testing | JUnit 5, Mockito |
| Build tool | Maven |

**Why Java and Spring Boot?**
Java's strong type system maps directly onto the UML class diagram from Assignment 9. Spring Boot eliminates boilerplate configuration, and Spring Data JPA generates repository implementations automatically from interface method names — meaning the seven repository interfaces require zero SQL to implement all basic CRUD operations.

**Why Lombok?**
Lombok removes repetitive getters, setters, constructors, and toString methods via annotations. `@Getter`, `@Setter`, `@NoArgsConstructor`, and `@Builder` are used instead of `@Data` to avoid constructor conflicts between Lombok and JPA.

---

### Creational Design Patterns

| Pattern | Class | ElectroView Use Case |
|---|---|---|
| Simple Factory | `NotificationFactory` | Creates ANOMALY_ALERT, BUDGET_WARNING, and REPORT_READY notifications from one central place — prevents scattered `new` calls across the codebase |
| Factory Method | `ReportExporter` → `PdfReportExporter` / `CsvReportExporter` | PDF and CSV exports share the same validation and processing workflow but differ in file production — subclasses decide the output format |
| Abstract Factory | `DashboardComponentFactory` → `AdminDashboardFactory` / `ConsumerDashboardFactory` | Each role gets a matching family of components — Admins get zone overview charts and anomaly panels, Consumers get personal usage charts and budget panels |
| Builder | `ReportRequest.Builder` | Report requests have two mandatory fields and several optional ones — the Builder enforces the mandatory fields and makes optional configuration readable |
| Prototype | `ZoneTemplateCache` + `ZonePrototype.clone()` | New zones are cloned from pre-configured RESIDENTIAL, INDUSTRIAL, or MIXED_USE templates instead of being configured from scratch each time |
| Singleton | `DatabaseConnectionManager` | One thread-safe connection pool is shared globally — multiple instances would exhaust database connections |

---
### API Endpoints

#### Users `/api/users`
| Method | URL | Description |
|---|---|---|
| POST | `/api/users` | Create a new user |
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| PATCH | `/api/users/{id}/activate` | Activate account |
| PATCH | `/api/users/{id}/deactivate` | Deactivate account |
| PATCH | `/api/users/{id}/unlock` | Unlock locked account |
| PATCH | `/api/users/{id}/role` | Update user role |
| PATCH | `/api/users/{id}/password` | Reset password |

#### Zones `/api/zones`
| Method | URL | Description |
|---|---|---|
| POST | `/api/zones` | Create a zone |
| GET | `/api/zones` | Get all zones |
| GET | `/api/zones/{id}` | Get zone by ID |
| GET | `/api/zones/active` | Get all active zones |
| PATCH | `/api/zones/{id}/activate` | Activate zone |
| PATCH | `/api/zones/{id}/deactivate` | Deactivate zone |
| PATCH | `/api/zones/{id}/threshold` | Update anomaly threshold |
| PATCH | `/api/zones/{id}/status` | Refresh zone status |

#### Meters `/api/meters`
| Method | URL | Description |
|---|---|---|
| POST | `/api/meters` | Register a meter |
| GET | `/api/meters` | Get all meters |
| GET | `/api/meters/{id}` | Get meter by ID |
| GET | `/api/meters/zone/{zoneId}` | Get meters in a zone |
| GET | `/api/meters/status/{status}` | Get meters by status |
| PATCH | `/api/meters/{id}/activate` | Activate meter |
| PATCH | `/api/meters/{id}/deactivate` | Deactivate meter |
| PATCH | `/api/meters/{id}/decommission` | Decommission meter |
| PATCH | `/api/meters/{id}/consumer` | Assign consumer to meter |

#### Readings `/api/readings`
| Method | URL | Description |
|---|---|---|
| POST | `/api/readings` | Ingest a meter reading |
| GET | `/api/readings/meter/{meterId}` | Get readings for a meter |
| GET | `/api/readings/meter/{meterId}/range` | Get readings in date range |
| GET | `/api/readings/anomalous` | Get all anomalous readings |

#### Anomalies `/api/anomalies`
| Method | URL | Description |
|---|---|---|
| GET | `/api/anomalies` | Get all anomalies |
| GET | `/api/anomalies/{id}` | Get anomaly by ID |
| GET | `/api/anomalies/status/{status}` | Get by status |
| GET | `/api/anomalies/meter/{meterId}` | Get by meter |
| GET | `/api/anomalies/count/open` | Count open anomalies |
| PATCH | `/api/anomalies/{id}/assign` | Assign to user |
| PATCH | `/api/anomalies/{id}/resolve` | Resolve anomaly |
| PATCH | `/api/anomalies/{id}/escalate` | Escalate anomaly |
| PATCH | `/api/anomalies/{id}/auto-resolve` | Auto-resolve anomaly |

#### Reports `/api/reports`
| Method | URL | Description |
|---|---|---|
| POST | `/api/reports` | Request a report |
| GET | `/api/reports/{id}` | Get report by ID |
| GET | `/api/reports/user/{userId}` | Get reports by user |
| GET | `/api/reports/status/{status}` | Get reports by status |
| PATCH | `/api/reports/{id}/purge` | Purge expired report |

#### Dashboard `/api/dashboard`
| Method | URL | Description |
|---|---|---|
| POST | `/api/dashboard/zones/{zoneId}/summarise` | Compute daily summary |
| GET | `/api/dashboard/zones/{zoneId}/summaries` | Get all zone summaries |
| GET | `/api/dashboard/zones/{zoneId}/summaries/range` | Get summaries in range |

### Test Results

| Test Class | Tests | What is verified |
|---|---|---|
| `UserServiceTest` | 9 | Account creation, lockout, activation, password reset |
| `ZoneServiceTest` | 7 | Zone creation, threshold updates, status changes |
| `MeterServiceTest` | 6 | Registration, activation, decommission, consumer assignment |
| `AnomalyServiceTest` | 9 | Create, assign, resolve, escalate, auto-resolve, count |
| `ReportServiceTest` | 6 | Inline vs queued reports, invalid date range, find by user |
| `NotificationFactoryTest` | 9 | Type correctness, message content, mark-as-read, unique IDs |
| `ReportExporterTest` | 8 | MIME type, CSV header, file naming, empty and null data |
| `DashboardComponentFactoryTest` | 7 | Chart type, alert panel capacity, role family isolation |
| `ReportRequestBuilderTest` | 7 | Minimal build, full build, invalid inputs, null dates |
| `ZonePrototypeTest` | 7 | Config equality, unique IDs, clone independence, cache |
| `DatabaseConnectionManagerTest` | 8 | Same instance, pool exhaustion, thread safety, shutdown |

---

## Project Board

ElectroView uses a GitHub Project (Team Planning template) as its Agile Kanban board.
The board tracks all user stories and sprint tasks across seven columns:

Backlog → Ready → In Progress → Testing → Blocked → Done

Two custom columns were added beyond the default template:
- **Testing** — ensures all tasks pass the test cases defined in TEST_CASES.md
  before entering code review, making QA a visible and mandatory workflow stage.
- **Blocked** — surfaces tasks that cannot proceed due to dependencies or unresolved
  decisions, separating them from active work to keep WIP counts accurate.

WIP limits: In Progress (max 4), Testing (max 3).
All Sprint 1 issues are linked to the milestone: Sprint 1 — MVP Foundation.

![alt text](<Screenshot (83).png>)
![alt text](<Screenshot (84).png>)

---

## Tech Stack (Planned — Frontend)

- **Frontend:** React + TypeScript, Chart.js / Recharts
- **Backend:** Node.js / Express REST API
- **Database:** PostgreSQL (time-series meter data) + Redis (caching)
- **Auth:** JWT-based role authentication
- **Deployment:** Docker + GitHub Actions CI/CD

---