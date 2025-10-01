# API-First Conference Project

Проект демонстрирует простую архитектуру микросервисов с использованием Spring Boot и Kotlin.

## Архитектура

Высокоуровнево можно посомтреть, запустив structurizr с помощью команды:
```bash
./run-c4.sh
```

### Сервисы

- **Admin Service** (`admin-service-app`) - REST API для управления отчетами
- **Report Generator Service** (`report-generator-service-app`) - REST API для генерации отчетов

### Технологический стек

- **Backend**: Spring Boot 3.5.3, Kotlin 2.0.0
- **API**: REST API (JSON)
- **Testing**: JUnit 5
- **Infrastructure**: Docker, Docker Compose

## Быстрый старт

### Предварительные требования

- Java 21+
- Docker & Docker Compose

### Запуск сервисов

```bash
# Запуск всех сервисов через Docker Compose
docker compose up -d

# Или запуск отдельных сервисов через Gradle
./gradlew admin-service:admin-service-app:bootRun
./gradlew report-generator-service:report-generator-service-app:bootRun
```

### Доступные сервисы

- **Admin Service**: http://localhost:8080
- **Report Generator Service**: http://localhost:8081

### API Endpoints

- **Admin Service**: `GET /api/admin/hello`
- **Report Generator Service**: `GET /api/report/hello`
