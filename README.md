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
- **API Documentation**: OpenAPI 3.0.3, Swagger UI
- **Code Generation**: OpenAPI Generator 7.14.0
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

## API Documentation

### Swagger UI

Интерактивная документация API доступна через Swagger UI:

- **Admin Service**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI Specification**: http://localhost:8080/v3/api-docs

### Code Generation

#### Генерация классов из OpenAPI контрактов

```bash
# Генерация Kotlin классов для Admin Service
./gradlew admin-service:admin-service-app:generateOpenApi
```

#### Структура сгенерированного кода

- **API Interfaces**: `build/generated/src/main/kotlin/home/kali/admin/generated/api/`
- **Models**: `build/generated/src/main/kotlin/home/kali/admin/generated/model/`
- **OpenAPI Contracts**: `docs/contracts/openapi-admin-service.yaml`

#### Конфигурация генерации

Генерация настраивается в `build.gradle.kts`:

```kotlin
tasks.register<GenerateTask>("generateOpenApi") {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/docs/contracts/openapi-admin-service.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated")
    apiPackage.set("home.kali.admin.generated.api")
    modelPackage.set("home.kali.admin.generated.model")
    invokerPackage.set("home.kali.admin.generated.invoker")
}
```

### API-First Workflow

1. **Создание контракта**: Определение API в OpenAPI YAML файле
2. **Генерация кода**: Автоматическая генерация Kotlin интерфейсов и моделей
3. **Реализация**: Реализация сгенерированных интерфейсов в контроллерах
4. **Документация**: Автоматическая генерация Swagger UI из контрактов
