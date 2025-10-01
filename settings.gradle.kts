rootProject.name = "api-first"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

include(
    "admin-service:admin-service-app",
    "report-generator-service:report-generator-service-api",
    "report-generator-service:report-generator-service-app",
    "report-generator-service:report-generator-service-sdk",
    "report-generator-service:report-generator-service-starter"
)
