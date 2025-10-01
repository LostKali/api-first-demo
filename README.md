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
- **API Documentation**: OpenAPI 3.0.3, Swagger UI (springdoc-openapi 2.7.0)
- **Code Generation**: OpenAPI Generator 7.14.0
- **HTTP Clients**: Spring HTTP Exchange, OpenFeign
- **Testing**: JUnit 5, Pact (Consumer-Driven Contract Testing)
- **Infrastructure**: Docker, Docker Compose, PostgreSQL 17.5

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
- **Pact Broker**: http://localhost:9292

### API Endpoints

- **Admin Service**: `GET /api/admin/hello`
- **Report Generator Service**: `GET /api/report/hello`

## API Documentation

### Swagger UI

Интерактивная документация API доступна через Swagger UI:

- **Admin Service**: http://localhost:8080/swagger-ui.html
- **Report Generator Service**: http://localhost:8081/swagger-ui.html
- **OpenAPI Specification**: 
  - Admin Service: http://localhost:8080/v3/api-docs
  - Report Generator Service: http://localhost:8081/v3/api-docs

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

## Contract Testing с Pact

Проект использует Pact для контрактного тестирования между сервисами, обеспечивая совместимость API между consumer (report-ui) и provider (admin-service).

### Pact Broker

Pact Broker запускается через Docker Compose и доступен по адресу:
- **URL**: http://localhost:9292
- **Username**: `pact`
- **Password**: `password`
- **Database**: PostgreSQL 17.5 (port 5435)

### Запуск всей инфраструктуры

```bash
# Запуск всех сервисов (admin-service, report-generator-service, Pact Broker, PostgreSQL)
docker compose up -d

# Запуск только Pact Broker для контрактного тестирования
docker compose up pact-broker pact-db -d
```

### Consumer Testing (Report UI)

Report UI выступает в роли consumer и тестирует взаимодействие с Admin Service API.

#### Команды для работы с Pact

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

#### Структура Pact тестов

- **Тестовые файлы**: `src/pact/admin-service.pact.test.js`
- **Фикстуры**: `src/pact/fixtures/interactions.js`, `src/pact/fixtures/test-data.js`
- **Сгенерированные контракты**: `pacts/ReportUI-AdminService.json`
- **Логи**: `logs/pact.log`

#### Покрываемые API endpoints

- `POST /api/admin/reports` - создание отчета
- `GET /api/admin/reports` - получение списка отчетов
- `GET /api/admin/reports/{reportId}` - получение деталей отчета
- `DELETE /api/admin/reports/{reportId}` - отмена отчета
- `GET /api/admin/reports/{reportId}/download` - скачивание отчета

### Provider Testing (Admin Service)

Admin Service выступает в роли provider и верифицирует соответствие контрактам.

#### Настройка Provider тестов

Provider тесты настроены в `build.gradle.kts`:

```kotlin
pact {
    broker {
        pactBrokerUrl = "http://localhost:9292"
        pactBrokerUsername = System.getenv("PACT_BROKER_USERNAME") ?: "pact"
        pactBrokerPassword = System.getenv("PACT_BROKER_PASSWORD") ?: "password"
    }
    
    serviceProviders {
        val provider = create("AdminService")
        provider.stateChangeUrl = uri("http://localhost:8080/pact/stateChange").toURL()
    }
    
    publish {
        pactBrokerUrl = "http://localhost:9292"
        version = "${project.version}"
    }
}
```

#### Запуск верификации

1. Запуск всей инфраструктуры (включая Pact Broker)
```bash
docker compose up -d
```
2. Запуск верификации (приложение запускается автоматически в рамках теста)
```bash
./gradlew admin-service:admin-service-app:pactProviderTest
```

#### Структура Provider тестов

- **Тестовый класс**: `src/test/kotlin/home/kali/admin/cdc/AdminServicePactProviderTest.kt`
- **State change методы**: Настроены для всех состояний из контрактов
- **Конфигурация**: Загружает контракты из Pact Broker автоматически

### Workflow контрактного тестирования

1. **Consumer тестирование (Report UI)**:
   - Генерирует контракты на основе ожидаемого поведения API
   - Публикует контракты в Pact Broker
   - Команда: `npm run pact:full`

2. **Provider верификация (Admin Service)**:
   - Загружает контракты из Pact Broker автоматически
   - Проверяет соответствие реального API сгенерированным контрактам
   - Публикует результаты верификации обратно в Pact Broker
   - Команда: `./gradlew pactProviderTest`

4. **Интеграция в CI/CD**:
   - Consumer тесты запускаются при изменении UI
   - Provider тесты запускаются при изменении API
   - Несовместимости выявляются на раннем этапе
   - Webhook уведомления о результатах верификации

### Просмотр контрактов и результатов

После публикации контракты и результаты верификации доступны в Pact Broker:

- **Главная страница**: http://localhost:9292
- **Конкретный контракт**: http://localhost:9292/pacts/provider/AdminService/consumer/ReportUI/version/1.0.1
- **Результаты верификации**: http://localhost:9292/pacts/provider/AdminService/consumer/ReportUI/version/1.0.1/verification-results

#### Учетные данные для доступа:
- **Username**: `pact`
- **Password**: `password`

### Быстрый старт контрактного тестирования

```bash
# 1. Запуск всей инфраструктуры (включая Pact Broker)
docker compose up -d

# 2. Consumer тестирование (Report UI)
```bash
cd report-ui
```

```bash
npm run pact:full
```

# 3. Provider тестирование (Admin Service)
```bash
cd ../admin-service/admin-service-app
```

```bash
./gradlew pactProviderTest
```

# 4. Просмотр результатов
open http://localhost:9292
