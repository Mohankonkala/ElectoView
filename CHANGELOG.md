# CHANGELOG

## [1.0.0] — May 2026

### Added

#### Domain Model
- `User.java` — User entity with account lockout after 5 failed login attempts,
  role management, and password reset validation
- `Zone.java` — Distribution zone with load percentage computation and
  configurable anomaly detection thresholds
- `Meter.java` — Smart meter with consecutive rejection fault detection
  and reading history
- `Reading.java` — Meter reading with validation and threshold comparison
- `Anomaly.java` — Anomaly lifecycle management including assignment,
  escalation, resolution, and auto-resolution
- `Report.java` — Report request with inline vs background processing
  detection and 24-hour expiry lifecycle
- `DailySummary.java` — Pre-aggregated daily zone consumption summary

#### Enumerations
- `Role` — ADMINISTRATOR, ANALYST, CONSUMER, TECHNICIAN, EXECUTIVE
- `AccountStatus` — PENDING, ACTIVE, LOCKED, INACTIVE
- `MeterStatus` — REGISTERED, ACTIVE, OFFLINE, FAULT_SUSPECTED,
  FAULT_CONFIRMED, UNDER_MAINTENANCE, DECOMMISSIONED
- `ZoneStatus` — CONFIGURED, NORMAL, HIGH_LOAD, ANOMALY_ALERT, INACTIVE
- `ReadingStatus` — RECEIVED, VALIDATING, PERSISTED, REJECTED
- `AnomalyStatus` — OPEN, IN_PROGRESS, ESCALATED, RESOLVED, AUTO_RESOLVED
- `ReportFormat` — PDF, CSV
- `ReportStatus` — REQUESTED, QUEUED, GENERATING, READY, FAILED, EXPIRED
- `ThresholdType` — ABSOLUTE, RELATIVE

#### Repositories
- `UserRepository` — findByEmail, existsByEmail, findByRole, findByStatus
- `ZoneRepository` — findByStatus, existsByName
- `MeterRepository` — findByZoneId, findByStatus, findBySerialNumber,
  findByConsumerId, existsBySerialNumber
- `ReadingRepository` — findByMeterId, findByMeterIdAndRecordedAtBetween,
  findByAnomalyTrue
- `AnomalyRepository` — findByMeterId, findByStatus, countByStatus,
  findByAssignedTo
- `ReportRepository` — findByRequestedBy, findByStatus, findByZoneId
- `DailySummaryRepository` — findByZoneId, findByZoneIdAndSummaryDate,
  findByZoneIdAndSummaryDateBetween

#### Service Layer
- `UserService` — Account creation, login tracking, lockout, role changes
- `ZoneService` — Zone creation, threshold configuration, status updates
- `MeterService` — Meter registration, activation, consumer assignment
- `ReadingService` — Ingests readings, validates, triggers anomaly detection
- `AnomalyService` — Creates, assigns, resolves, and escalates anomalies
- `ReportService` — Inline and background report generation
- `DailySummaryService` — Aggregates zone readings into daily summaries

#### REST Controllers
- `UserController` — `/api/users/**`
- `ZoneController` — `/api/zones/**`
- `MeterController` — `/api/meters/**`
- `ReadingController` — `/api/readings/**`
- `AnomalyController` — `/api/anomalies/**`
- `ReportController` — `/api/reports/**`
- `DashboardController` — `/api/dashboard/**`

#### Creational Design Patterns
- **Simple Factory** — `NotificationFactory` creates typed notification
  objects centrally, eliminating scattered instantiation across the codebase
- **Factory Method** — `ReportExporter` abstract class with `PdfReportExporter`
  and `CsvReportExporter` subclasses delegating file creation
- **Abstract Factory** — `DashboardComponentFactory` interface with
  `AdminDashboardFactory` and `ConsumerDashboardFactory` producing
  role-specific component families
- **Builder** — `ReportRequest.Builder` constructs complex report parameters
  with mandatory validation and optional field chaining
- **Prototype** — `ZoneTemplateCache` stores and clones pre-configured
  RESIDENTIAL, INDUSTRIAL, and MIXED_USE zone templates
- **Singleton** — `DatabaseConnectionManager` with thread-safe
  double-checked locking and connection pool management

#### Tests
- `UserServiceTest` — 9 tests
- `ZoneServiceTest` — 7 tests
- `MeterServiceTest` — 6 tests
- `AnomalyServiceTest` — 9 tests
- `ReportServiceTest` — 6 tests
- `NotificationFactoryTest` — 9 tests
- `ReportExporterTest` — 8 tests
- `DashboardComponentFactoryTest` — 7 tests
- `ReportRequestBuilderTest` — 7 tests
- `ZonePrototypeTest` — 7 tests
- `DatabaseConnectionManagerTest` — 8 tests
- **Total: 83 tests — 0 failures**

### Technical Decisions
- Used `@Getter`, `@Setter`, `@NoArgsConstructor`, and `@Builder` from
  Lombok instead of `@Data` to avoid constructor conflicts with JPA
- Used `@AfterEach` instead of `@BeforeEach` in singleton tests to prevent
  IllegalStateException on first test run
- Used `thenAnswer(invocation -> invocation.getArgument(0))` in Mockito
  tests where the service mutates the object before saving
- MySQL configured with `ddl-auto=update` so Hibernate manages the schema
  automatically during development
- Spring Security disabled in `application.properties` during development
  to allow unrestricted API access while building the core features