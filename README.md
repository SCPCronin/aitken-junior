# AitkenJunior — Personal Projects Platform

A **Spring Boot backbone** for personal projects — an extensible, well-structured backend designed to be quickly extended with new features without re-building infrastructure every time.

## 🏗️ Architecture

```
AitkenJunior/
├── src/main/java/org/example/
│   ├── Application.java               # Spring Boot entry point
│   ├── common/                        # Shared infrastructure (used by all modules)
│   │   ├── dto/ApiResponse.java       # Standardised API response wrapper
│   │   ├── entity/BaseEntity.java     # Base JPA entity (UUID id, createdAt, updatedAt)
│   │   └── exception/                 # Global exception handling
│   ├── health/                        # Health check module
│   │   └── controller/HealthController.java
│   ├── discord/                       # Discord bot module (PR 2)
│   └── {feature}/                     # Future feature modules (notes, quiz, etc.)
│       ├── entity/
│       ├── repository/
│       ├── service/
│       ├── dto/
│       └── controller/
├── src/main/resources/
│   ├── application.properties         # Base config (all environments)
│   ├── application-dev.properties     # Local dev overrides
│   ├── application-test.properties    # Test overrides (H2 in-memory DB)
│   └── db/migration/                  # Flyway SQL migrations (V1__, V2__, ...)
├── docker-compose.yml                 # PostgreSQL container for local dev
└── .env.example                       # Environment variable template
```

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+ (or use the included `./mvnw` wrapper)
- Docker Desktop (for the PostgreSQL container)

### 1. Set up environment variables

```bash
cp .env.example .env
# Edit .env with your values (Discord token, etc.)
```

### 2. Start the database

```bash
docker compose up -d postgres
# Wait for it to be healthy:
docker compose ps
```

### 3. Run the application

```bash
# With Maven wrapper (recommended):
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Or with environment variables exported:
source .env && ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. Verify it's running

```bash
curl http://localhost:8080/api/health
```

Expected response:
```json
{
  "success": true,
  "message": "Application is healthy",
  "data": {
    "status": "UP",
    "application": "AitkenJunior",
    "version": "1.0.0-SNAPSHOT",
    "timestamp": "2026-03-04T10:00:00"
  }
}
```

## 🧪 Running Tests

Tests use an H2 in-memory database — **no Docker required** for the test suite.

```bash
./mvnw test
```

## 📦 Database Migrations (Flyway)

Schema changes are managed with [Flyway](https://flywaydb.org/). Every change to the database schema **must** be a new numbered SQL file:

```
src/main/resources/db/migration/
├── V1__init_schema.sql         # Baseline — enables pgcrypto extension
├── V2__create_discord_users.sql  # (PR 2) Discord user tracking
├── V3__create_notes.sql          # (PR 3) Notes module
└── V4__create_quiz_tables.sql    # (PR 4) Quiz module
```

**Rules:**
- Never modify an existing migration that has already run on any database
- Always increment the version number
- Keep migrations small and focused

## 🤖 Discord Bot Setup (PR 2)

1. Go to [Discord Developer Portal](https://discord.com/developers/applications)
2. Create a new application → Bot → Reset Token
3. Copy the token into your `.env` file as `DISCORD_BOT_TOKEN`
4. Enable `discord.bot.enabled=true` in `application-dev.properties`
5. Invite the bot to your server using the OAuth2 URL Generator (scopes: `bot`, `applications.commands`)

## 🌱 Adding a New Feature Module

Each feature lives in its own package under `org.example.{feature}/` and follows this layered structure:

```
{feature}/
├── entity/         # JPA entity (extends BaseEntity)
├── repository/     # Spring Data JPA repository interface
├── service/        # Business logic — keep controllers thin
├── dto/            # Request/response DTOs with Bean Validation
└── controller/     # REST controller mapping to ApiResponse<T>
```

For Discord integration, add a `{FeatureName}CommandHandler` implementing `CommandHandler` (added in PR 2).

## 🗺️ Roadmap

| PR | Status | Description |
|----|--------|-------------|
| PR 1 | ✅ Done | Spring Boot foundation, PostgreSQL, Docker Compose, Flyway |
| PR 2 | 🔜 Next | Discord bot integration (JDA, slash commands, command router) |
| PR 3 | 📋 Planned | Notes module (Obsidian/Markdown compatible) |
| PR 4 | 📋 Planned | Daily pub quiz module |
| PR 5 | 📋 Planned | Frontend (separate repo — Next.js + Tailwind) |

## 🔗 Related Repositories

- **Frontend**: _Coming in PR 5_ — Next.js application displaying data from this API

