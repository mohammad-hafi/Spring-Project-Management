# Project Management API

A REST API for user and project management, built with Spring Boot. The service supports user registration and login, JWT-based authentication, permission-protected project operations, PostgreSQL persistence, database migrations, and observability through OpenTelemetry.

## Features

- User registration and login
- Stateless JWT authentication
- Role- and permission-based authorization
- Create, read, and update owned projects
- Request validation and consistent API error responses
- PostgreSQL schema management with Flyway
- Interactive OpenAPI documentation with Swagger UI
- Health and readiness endpoints
- Distributed tracing with Jaeger
- Structured log collection with Seq
- Unit, web, integration, and architecture tests

## Tech stack

- Java 17
- Spring Boot 4.1
- Spring Security
- Spring Data JPA
- PostgreSQL 17
- Flyway
- Gradle
- JUnit 5 and Testcontainers
- Docker and Docker Compose
- OpenTelemetry, Jaeger, and Seq

## Getting started with Docker Compose

### Prerequisites

- Docker with Docker Compose

### 1. Configure the environment

Copy `.env.example` to `.env`:

```powershell
Copy-Item .env.example .env
```

Set secure values for these required variables:

```dotenv
DB_PASSWORD=replace-with-a-database-password
JWT_SECRET=replace-with-a-long-random-secret
SEQ_ADMIN_PASSWORD=replace-with-a-seq-admin-password
```

`JWT_SECRET` should be a long, random value. Do not commit the populated `.env` file.

### 2. Start the stack

```shell
docker compose up --build
```

The following services become available:

| Service | Default URL |
| --- | --- |
| API | http://localhost:9090 |
| Swagger UI | http://localhost:9090/swagger-ui/index.html |
| API health | http://localhost:9090/actuator/health |
| Seq | http://localhost:5341 |
| Jaeger | http://localhost:16686 |

To stop the services:

```shell
docker compose down
```

Add `-v` only when you also want to remove the PostgreSQL and Seq data volumes.

## Running locally

### Prerequisites

- Java 17
- Docker, or a local PostgreSQL instance

Start only PostgreSQL if you want the application to run from Gradle:

```shell
docker compose up -d postgres
```

Make sure the database credentials match your environment, then run:

```powershell
$env:DB_PASSWORD = "your-database-password"
./gradlew.bat bootRun
```

On macOS or Linux:

```shell
DB_PASSWORD=your-database-password ./gradlew bootRun
```

The default `dev` profile supplies a development-only JWT secret. Use `JWT_SECRET` and the `prod` profile for production-like environments.

## API usage

### Register a user

```shell
curl -X POST http://localhost:9090/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alex Doe",
    "description": "Backend developer",
    "email": "alex@example.com",
    "department": "Engineering",
    "password": "Password1!",
    "jobTitle": "Developer",
    "statusId": "ACTIVE"
  }'
```

The initial migration creates status `1` (`ACTIVE`) and assigns newly registered users the default `MEMBER` role.

### Log in

```shell
curl -X POST http://localhost:9090/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alex@example.com",
    "password": "Password1!"
  }'
```

The response contains a JWT access token:

```json
{
  "token": "<jwt>"
}
```

### Create a project

Use the token in the `Authorization` header. `targetDate` must be a future ISO-8601 timestamp, and `priorityLevel` must be between 1 and 5.

```shell
curl -X POST http://localhost:9090/project \
  -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Website redesign",
    "description": "Refresh the company website",
    "targetDate": "2027-01-31T12:00:00Z",
    "priorityLevel": 2,
    "statusId": "ACTIVE"
  }'
```

### Get or update a project

```shell
curl -H "Authorization: Bearer <jwt>" \
  http://localhost:9090/project/1
```

```shell
curl -X PUT http://localhost:9090/project/1 \
  -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Website redesign",
    "description": "Updated project scope",
    "targetDate": "2027-02-28T12:00:00Z",
    "priorityLevel": 1,
    "statusId": "ACTIVE"
  }'
```

## Endpoint summary

| Method | Path | Authentication | Description |
| --- | --- | --- | --- |
| `POST` | `/user/register` | Public | Register a user |
| `POST` | `/user/login` | Public | Log in and receive a JWT |
| `POST` | `/project` | Bearer token | Create a project |
| `GET` | `/project/{id}` | Bearer token | Get an owned project |
| `PUT` | `/project/{id}` | Bearer token | Update an owned project |
| `GET` | `/actuator/health` | Public | Application health |

For full request and response schemas, use Swagger UI after starting the application.

## Configuration

| Variable | Default | Purpose |
| --- | --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/ProjectManagment` | JDBC connection URL |
| `DB_NAME` | `ProjectManagment` | Docker PostgreSQL database name |
| `DB_USERNAME` | `postgres` | Database user |
| `DB_PASSWORD` | none | Database password |
| `JWT_SECRET` | dev profile only | JWT signing secret |
| `JWT_ISSUER` | `project-management-api` | JWT issuer |
| `SERVER_PORT` | `9090` | Application port |
| `DB_POOL_SIZE` | `10` | Production connection-pool size |
| `OTEL_TRACES_ENDPOINT` | `http://localhost:4318/v1/traces` | OTLP trace endpoint |
| `OTEL_LOGS_ENDPOINT` | `http://localhost:5341/ingest/otlp/v1/logs` | OTLP log endpoint |

Port overrides for Docker Compose are listed in `.env.example`.

## Tests

Run the test suite with:

```powershell
./gradlew.bat test
```

On macOS or Linux:

```shell
./gradlew test
```

The Testcontainers integration test runs when Docker is available and is skipped otherwise. The HTML test report is generated at `build/reports/tests/test/index.html`.

## Project structure

```text
src/main/java/com/example/projectmanagement/
├── Application/       # Use cases, security, services, DTOs, and configuration
├── Domain/            # Entities, enums, and domain exceptions
├── Infrastructure/    # JPA repositories and persistence adapters
└── Presentation/      # REST controllers, filters, and API error handling

src/main/resources/
├── db/migration/      # Flyway database migrations
├── application.properties
├── application-dev.properties
└── application-prod.properties
```

## Building a runnable JAR

```powershell
./gradlew.bat clean bootJar
```

The generated JAR is placed in `build/libs/`.
