# Enterprise Identity Platform

A production-grade microservices system built with Java 17, Spring Boot, Kafka, Keycloak, PostgreSQL and Docker.  
Designed to demonstrate senior-level backend engineering, system design, and cloud-native architecture.

---

## Architecture

The system is composed of multiple microservices and infrastructure components:

enterprise-identity-platform/
├── core-service # Main business service (REST, OpenAPI, Security, JPA)
├── kafka-processor # Kafka consumer / async processor
├── soap-adapter # SOAP → REST adapter
├── docker-compose.yml # Full local infrastructure
├── .env.example # Environment template

yaml

Infrastructure:
- PostgreSQL
- Apache Kafka (KRaft mode)
- Keycloak (OAuth2 / OIDC)
- Docker Compose

---

## Tech Stack

**Language & Frameworks**
- Java 17
- Spring Boot 3
- Spring Web
- Spring Security (OAuth2 Resource Server)
- Spring Data JPA
- Spring Actuator

**Architecture**
- Microservices
- Clean Architecture (Controller → Application → Domain → Infrastructure)
- Contract-first APIs (OpenAPI)
- Asynchronous messaging (Kafka)

**Infrastructure**
- Docker & Docker Compose
- PostgreSQL
- Apache Kafka
- Keycloak

**Engineering Practices**
- OpenAPI Generator
- MapStruct
- Profiles (local / docker)
- Healthchecks
- Structured logging
- Correlation ID filter

---

## Services

| Service          | Port | Description |
|------------------|------|--------------|
| core-service     | 8080 | Main REST API |
| kafka-processor  | -    | Kafka consumer / worker |
| soap-adapter     | 8082 | SOAP integration adapter |
| keycloak         | 8081 | Identity provider |
| postgres         | 5433 | Database |
| kafka            | 9092 / 9094 | Messaging broker |

---

## Requirements

- Docker
- Docker Compose

(Optional if running without containers)
- Java 17
- Maven 3.9+

---

## Environment Setup

Copy the environment template:

```
cp .env.example .env
Edit .env with your values:

POSTGRES_DB=enterprise
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=admin

KEYCLOAK_CLIENT_SECRET=your-client-secret

CORE_SERVICE_USER=soapuser
CORE_SERVICE_PASS=soappass
Build (optional)
If you want to build jars manually:

mvn clean package -DskipTests
Otherwise Docker Compose will build everything automatically.

Run the entire system
From project root:

docker compose up --build
After startup:

Core Service: http://localhost:8080

Soap Adapter: http://localhost:8082

Keycloak: http://localhost:8081

PostgreSQL: localhost:5433

Kafka (external): localhost:9094

Healthcheck:

http://localhost:8080/actuator/health
Keycloak Setup
Open http://localhost:8081

Login using:

user: admin

password: admin

Create Realm: enterprise

Create Client:

Client ID: core-service

Type: OpenID Connect

Access Type: confidential

Generate client secret and put it into .env:

KEYCLOAK_CLIENT_SECRET=...
Core-service is configured as OAuth2 Resource Server.

Why this project exists
This project was built to demonstrate real-world backend engineering skills:

Designing microservices

Building scalable architectures

Using Kafka for async processing

Securing services with OAuth2 (Keycloak)

Writing clean layered architecture

Working with Dockerized infrastructure

Applying production patterns (healthchecks, logging, profiles, contracts)

It reflects how systems are built in real companies, not tutorial-style code.

Author
Elias Bouras
Software Engineer
GitHub: https://github.com/eliasss01
