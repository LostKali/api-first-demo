plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Disable bootJar for starter module since it's not a runnable application
tasks.bootJar {
    enabled = false
}

dependencies {
    api(project(":report-generator-service:report-generator-service-api"))
    
    api("org.springframework.boot:spring-boot-starter")

    api(platform("org.springframework.cloud:spring-cloud-dependencies:2025.0.0"))
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("io.github.openfeign:feign-okhttp")
    api("io.github.openfeign:feign-jackson")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}