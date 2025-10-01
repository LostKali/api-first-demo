# API-First Conference Project

Проект демонстрирует подход API-First для разработки микросервисов с использованием Spring Boot и Kotlin.

## Архитектура

### Визуализация

Для просмотра архитектуры запустите structurizr:
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
- **API**: REST API (JSON), gRPC
- **Documentation**: OpenAPI 3.0.3, Swagger UI (springdoc-openapi 2.7.0)
- **Code Generation**: OpenAPI Generator 7.14.0
- **HTTP Clients**: Spring HTTP Exchange, OpenFeign
- **Testing**: JUnit 5, Pact (Consumer-Driven Contract Testing)
- **Infrastructure**: Docker, Docker Compose, PostgreSQL 17.5

## Быстрый старт

### Предварительные требования

- Java 21+
- Docker & Docker Compose

### Запуск сервисов

**Все сервисы через Docker Compose:**
```bash
docker compose up -d
```

**Отдельные сервисы через Gradle:**
```bash
./gradlew admin-service:admin-service-app:bootRun
./gradlew report-generator-service:report-generator-service-app:bootRun
```

### Доступные сервисы

- **Admin Service**: http://localhost:8080
- **Report Generator Service**: http://localhost:8081
- **Pact Broker**: http://localhost:9292

## API Documentation

### Swagger UI

Интерактивная документация API:

- **Admin Service**: http://localhost:8080/swagger-ui.html
- **Report Generator Service**: http://localhost:8081/swagger-ui.html

### OpenAPI Specifications

- **Admin Service**: http://localhost:8080/v3/api-docs
- **Report Generator Service**: http://localhost:8081/v3/api-docs

### Code Generation

**Admin Service:**
```bash
./gradlew admin-service:admin-service-app:generateOpenApi
```

**Report Generator Service:**
```bash
./gradlew report-generator-service:report-generator-service-api:generateOpenApi
```

### Структура сгенерированного кода

**Admin Service:**
- **API Interfaces**: `build/generated/src/main/kotlin/home/kali/admin/generated/api/`
- **Models**: `build/generated/src/main/kotlin/home/kali/admin/generated/model/`
- **OpenAPI Contract**: `docs/contracts/openapi-admin-service.yaml`

**Report Generator Service:**
- **API Interfaces**: `build/generated/src/main/kotlin/home/kali/report/generated/api/`
- **Models**: `build/generated/src/main/kotlin/home/kali/report/generated/model/`
- **OpenAPI Contract**: `docs/contracts/openapi-report-generator.yaml`

### Конфигурация генерации

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

## Contract Testing с Pact

Проект использует Pact для контрактного тестирования между сервисами:

1. **REST API**: Report UI (consumer) ↔ Admin Service (provider)
2. **gRPC API**: Admin Service (consumer) ↔ Report Generator Service (provider)

### Pact Broker

- **URL**: http://localhost:9292
- **Username**: `pact`
- **Password**: `password`
- **Database**: PostgreSQL 17.5 (port 5435)

**Запуск только Pact Broker:**
```bash
docker compose up pact-broker pact-db -d
```


## Consumer Testing

### Report UI → Admin Service (REST API)

**Команды:**
```bash
cd report-ui

# Полный цикл: очистка → тестирование → публикация
npm run pact:full

# Только генерация контрактов
npm run test:pact

# Только публикация контрактов
npm run pact:publish

# Очистка артефактов
npm run pact:clean
```

**Структура:**
- **Тестовые файлы**: `src/pact/admin-service.pact.test.js`
- **Фикстуры**: `src/pact/fixtures/interactions.js`, `src/pact/fixtures/test-data.js`
- **Контракты**: `pacts/ReportUI-AdminService.json`

**Покрываемые endpoints:**
- `POST /api/admin/reports` - создание отчета
- `GET /api/admin/reports` - получение списка отчетов
- `GET /api/admin/reports/{reportId}` - получение деталей отчета
- `DELETE /api/admin/reports/{reportId}` - отмена отчета
- `GET /api/admin/reports/{reportId}/download` - скачивание отчета

### Admin Service → Report Generator Service (gRPC API)

**Команды:**
```bash
# Consumer тесты (генерация gRPC контрактов)
./gradlew admin-service:admin-service-app:pactConsumerTest

# Публикация контрактов
./gradlew admin-service:admin-service-app:pactPublish
```

**Структура:**
- **Тестовый класс**: `src/test/kotlin/home/kali/admin/cdc/ReportGeneratorConsumerTest.kt`
- **Контракты**: `build/pacts/AdminService-ReportGeneratorService.json`

**Покрываемые gRPC методы:**
- `GenerateReport` - создание запроса на генерацию отчета
- `GetReportStatus` - получение статуса генерации отчета
- `CancelReport` - отмена генерации отчета
- `DownloadReport` - скачивание готового отчета

## Provider Testing

### Admin Service Provider Tests (REST API)

**Запуск:**
```bash
./gradlew admin-service:admin-service-app:pactProviderTest
```

**Структура:**
- **Тестовый класс**: `src/test/kotlin/home/kali/admin/cdc/AdminServicePactProviderTest.kt`
- **State change методы**: Настроены для всех состояний из контрактов
- **Конфигурация**: Загружает контракты из Pact Broker автоматически

### Report Generator Service Provider Tests (gRPC API)

**Запуск:**
```bash
./gradlew report-generator-service:report-generator-service-app:pactProviderTest
```

## Gradle Tasks

### Admin Service
```bash
# Consumer тесты (gRPC контракты)
./gradlew admin-service:admin-service-app:pactConsumerTest

# Provider тесты (REST контракты)
./gradlew admin-service:admin-service-app:pactProviderTest

# Публикация контрактов
./gradlew admin-service:admin-service-app:pactPublish

# Полный цикл: consumer + provider + publish
./gradlew admin-service:admin-service-app:pactTest
```

### Report Generator Service
```bash
# Provider тесты (gRPC контракты)
./gradlew report-generator-service:report-generator-service-app:pactProviderTest
```

## Workflow контрактного тестирования

1. **REST API Consumer** (Report UI → Admin Service):
   ```bash
   cd report-ui
   npm run pact:full
   ```

2. **gRPC Consumer** (Admin Service → Report Generator Service):
   ```bash
   ./gradlew admin-service:admin-service-app:pactConsumerTest
   ./gradlew admin-service:admin-service-app:pactPublish
   ```

3. **REST Provider** (Admin Service):
   ```bash
   ./gradlew admin-service:admin-service-app:pactProviderTest
   ```

4. **gRPC Provider** (Report Generator Service):
   ```bash
   ./gradlew report-generator-service:report-generator-service-app:pactProviderTest
   ```

### Просмотр результатов

- **Pact Broker**: http://localhost:9292
- **REST контракты**: http://localhost:9292/pacts/provider/AdminService/consumer/ReportUI
- **gRPC контракты**: http://localhost:9292/pacts/provider/ReportGeneratorService/consumer/AdminService
