workspace "Report Generation System" "API-First Report Generation Architecture" {

    model {
        group "Report Generation Services" {
            reportAdminFrontend = softwareSystem "Report Admin Frontend" {
                adminDashboard = container "Admin Frontend" "Web UI" "UI для управления отчетами"
            }
            
            reportAdminBackend = softwareSystem "Report Admin Backend" {
                description "Основной сервис для управления отчетами, пишется с нуля"

                adminApi = container "Admin Backend" "Kotlin/Spring Boot" "REST API для управления отчетами" {
                    reportController = component "Report Controller" "Предоставляет endpoint'ы для работы с отчетами" "Spring MVC Rest Controller"
                    reportRequestSaver = component "Report Request Saver" "Интерфейс, определяющий методы для запроса на генерацию отчета" "Java interface"
                    reportRequestSaverImpl = component "Реализация Report Request Saver" "" "Java interface"
                    reportProcessor = component "Report Generation Processor" "Интерфейс, определяющий методы ЖЦ генерации отчета" "Java interface"
                    reportProcessorImpl = component "Реализация Report Generation Processor" "" "Java interface"
                    asyncProcessor = component "Async Processor" "Обработчик жц генерации отчета" "" 
                }
                dbA = container "Admin DB" "PostgreSQL" "База данных админки для хранения заявок и статусов отчетов" "Database"
            }

            adminBackendDoppelganger = softwareSystem "Report Admin Backend Doppleganger" {
                description "Основной сервис для управления отчетами, пишется с нуля"

                adminApiDpg = container "Admin Backend" "Kotlin/Spring Boot" "REST API для управления отчетами" {
                    reportControllerDpg = component "Report Controller" "Предоставляет endpoint'ы для работы с отчетами" "Spring MVC Rest Controller"
                    reportServiceDpg = component "Report Service" "Логика по генерации отчета" "Spring Bean"
                }
                dbADpg = container "Admin DB" "PostgreSQL" "База данных админки для хранения заявок и статусов отчетов" "Database"
            }

            reportGeneratorBackend = softwareSystem "Report Generator" {
                description "Сервис генерации отчетов, может работать до 15 минут"

                reportApi = container "Report Generator API" "Kotlin/Spring Boot" "API для приема задач на генерацию отчетов"
                jobQueue = container "Report Queue" "RabbitMQ / SQS / Kafka" "Асинхронная очередь для обработки задач" "Message Queue"
                reportWorker = container "Report Worker" "Kotlin/Spring Worker" "Воркер для генерации отчетов (до 15 мин)"
                dbB = container "Raw Data DB" "PostgreSQL/BigQuery/etc." "База с исходными данными для отчетов" "Database"
            }
        }

        user = person "Admin User" {
            description "Администратор, который генерирует отчеты через UI"
        }

        # Relationships
        user -> adminDashboard "Хочу отчет"
        adminDashboard -> reportController "Отправляет запрос на генерацию отчета"
        reportController -> reportRequestSaver ""
        reportRequestSaver -> reportRequestSaverImpl ""
        reportRequestSaverImpl -> dbA "Читает и записывает заявки на отчеты"
        asyncProcessor -> reportProcessor "Запускает обработку ЖЦ отчета"
        reportProcessor -> reportProcessorImpl
        reportProcessorImpl -> reportApi "Отправляет задачу на генерацию отчета"
        reportProcessorImpl -> dbA "Читает и записывает информацию о генерации отчета"
        
        # Doppleganger
        adminDashboard -> reportControllerDpg "Отправляет запрос на генерацию отчета"
        reportControllerDpg -> reportServiceDpg ""
        reportServiceDpg -> dbADpg "Читает и записывает заявки на отчеты"
        reportServiceDpg -> reportApi "Отправляет задачу на генерацию отчета"

        reportApi -> jobQueue "Кладёт задачу в очередь (до 15 мин)"
        reportWorker -> jobQueue "Читает задачи из очереди"
        reportWorker -> dbB "Читает исходные данные для отчета"
        reportWorker -> adminApi "Уведомляет о завершении генерации"
    }

    views {
        systemLandscape "SystemLandscape" {
            include user
            include reportAdminFrontend
            include reportAdminBackend
            include reportGeneratorBackend
        }

        systemContext reportAdminBackend "SystemContext" {
            include *
            include user
            include reportAdminFrontend
        }

        container reportAdminBackend "ReportAdminBackend" {
            include *
            include user
            include reportAdminFrontend
        }

        container reportGeneratorBackend "ReportGeneratorBackend" {
            include *
        }

        component adminApi "ReportAdminApi" {
            include *
        }

        component adminApiDpg "ReportAdminApiDpg" {
            include *
        }

        dynamic reportAdminBackend "ReportGeneration" "Показывает процесс генерации отчета (до 15 минут)" {
            adminApi -> dbA "1. Сохраняет заявку на отчет"
            adminApi -> reportApi "2. Отправляет задачу на генерацию"
            reportApi -> jobQueue "3. Кладёт задачу в очередь"
            reportWorker -> jobQueue "4. Получает задачу (до 15 мин)"
            reportWorker -> dbB "5. Читает исходные данные"
            reportWorker -> adminApi "6. Уведомляет о завершении генерации"
            adminApi -> dbA "7. Обновляет статус отчета"
        }

        styles {
            element "Software System" {
                background #1168bd
                color #ffffff
            }
            element "Container" {
                background #438dd5
                color #ffffff
            }
            element "Person" {
                background #08427b
                color #ffffff
            }
            element "Database" {
                background #D29F51
                color #ffffff
                shape Cylinder
            }
            element "Message Queue" {
                background #D29F51
                shape Pipe
                color #ffffff
            }
        }

        theme default
    }
}