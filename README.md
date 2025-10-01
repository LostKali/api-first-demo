# API-First Conference Project

Проект демонстрирует простую архитектуру микросервисов с использованием Spring Boot и Kotlin.

## Архитектура

Высокоуровнево можно посомтреть, запустив structurizr с помощью команды:
```bash
./run-c4.sh
```

### Сервисы

- **Admin Service** (`admin-service-app`) - REST API для управления отчетами
- **Report Generator Service** - Модульная архитектура для генерации отчетов:
  - `report-generator-service-api` - API интерфейсы и модели
  - `report-generator-service-app` - REST API приложение
  - `report-generator-service-sdk` - SDK для клиентов
  - `report-generator-service-starter` - Spring Boot Starter

### Технологический стек

- **Backend**: Spring Boot 3.5.3, Kotlin 2.0.0
- **API**: REST API (JSON)
- **API Documentation**: OpenAPI 3.0.3, Swagger UI
- **Code Generation**: OpenAPI Generator 7.14.0
- **HTTP Clients**: Spring HTTP Exchange, OpenFeign
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

# Генерация Kotlin классов для Report Generator Service
./gradlew report-generator-service:report-generator-service-api:generateOpenApi
```

#### Структура сгенерированного кода

**Admin Service:**
- **API Interfaces**: `build/generated/src/main/kotlin/home/kali/admin/generated/api/`
- **Models**: `build/generated/src/main/kotlin/home/kali/admin/generated/model/`
- **OpenAPI Contract**: `docs/contracts/openapi-admin-service.yaml`

**Report Generator Service:**
- **API Interfaces**: `build/generated/src/main/kotlin/home/kali/report/generated/api/`
- **Models**: `build/generated/src/main/kotlin/home/kali/report/generated/model/`
- **OpenAPI Contract**: `docs/contracts/openapi-report-generator.yaml`

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

## Модульная архитектура Report Generator Service

### Зачем нужны 4 модуля?

**1. `report-generator-service-api`** - API контракты
- Генерирует Kotlin интерфейсы и модели из OpenAPI контракта
- Определяет API endpoints и модели данных
- Содержит HttpExchange интерфейсы
- Используется как зависимость другими модулями

**2. `report-generator-service-app`** - REST API приложение
- Реализует API интерфейсы из `report-generator-service-api`
- Содержит контроллеры и бизнес-логику
- Запускаемое Spring Boot приложение

**3. `report-generator-service-sdk`** - SDK для клиентов
- Предоставляет готовые HTTP клиенты для взаимодействия с API
- Поддерживает два типа клиентов: OpenFeign и HTTP Exchange
- Автоматическая конфигурация через Spring Boot

**4. `report-generator-service-starter`** - Spring Boot Starter
- Автоматическая конфигурация SDK при добавлении зависимости
- Упрощает интеграцию для клиентских приложений
- Следует паттерну Spring Boot Starters

### HTTP Exchange Client Example

```kotlin
// Конфигурация HTTP Exchange клиента
@Profile("http-exchange")
@Configuration
class InternalReportGeneratorHttpExchangeClientConfiguration {
    @Bean
    fun reportGeneratorClient(@Value("\${report-generator.url}") url: String): InternalHttpExchangeReportGeneratorApi {
        val client = RestClient.builder().baseUrl(url).build()
        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(client)).build()
            .createClient(InternalHttpExchangeReportGeneratorApi::class.java)
    }
}

// Использование в сервисе
@Service
class ReportService(
    private val reportGeneratorClient: InternalHttpExchangeReportGeneratorApi
) {
    fun generateReport(request: GenerateReportRequest): GenerateReportResponse {
        return reportGeneratorClient.generateReport(request)
    }
}
```

### OpenFeign Client Example

```kotlin
// Конфигурация OpenFeign клиента
@FeignClient(
    value = "report-generator-client",
    url = "\${report-generator.url}",
    configuration = [InternalReportGeneratorClientConfiguration::class]
)
interface InternalReportGeneratorFeignClient : ReportGenerationApi

// Использование в сервисе
@Service
class ReportService(
    private val reportGeneratorClient: InternalReportGeneratorFeignClient
) {
    fun generateReport(request: GenerateReportRequest): GenerateReportResponse {
        return reportGeneratorClient.generateReport(request)
    }
}
```
