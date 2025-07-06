# Spring Boot Boilerplate

This project is a **Spring Boot** boilerplate designed to help you kickstart a modern web application quickly.  
It includes user management, JWT authentication, Liquibase migrations, and Docker support out of the box.

---

## 🚀 Features

- Spring Boot 3.x
- JWT authentication (access + refresh tokens)
- Liquibase for database migrations
- PostgreSQL support (Docker-ready)
- Redis integration (optional, for token management)
- Global exception handling
- Pre-configured `Makefile`
- Clean layered architecture (Controller, Service, Repository)

---

## 🛠 Setup

### Requirements
- Java 17+
- Docker & Docker Compose
- Maven Wrapper (`./mvnw`)

### Run the project

```bash
# Start PostgreSQL with Docker
docker compose up -d

# Apply Liquibase migrations
make clear

# Run the Spring Boot app
make run