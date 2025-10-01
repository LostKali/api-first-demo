plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("au.com.dius.pact") version "4.3.10"
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

// Pact configuration
pact {
    broker {
        pactBrokerUrl = "http://localhost:9292"
        pactBrokerUsername = System.getenv("PACT_BROKER_USERNAME") ?: "pact"
        pactBrokerPassword = System.getenv("PACT_BROKER_PASSWORD") ?: "password"
    }
    
    serviceProviders {
        create("ReportGeneratorService") {
            stateChangeUrl = uri("http://localhost:8081/pact/stateChange").toURL()
        }
    }
    
    publish {
        pactBrokerUrl = "http://localhost:9292"
        version = "${project.version}"
    }
}

// Provider tests (verifies contracts)
tasks.register<Test>("pactProviderTest") {
    group = "verification"
    description = "Runs Pact provider verification tests"
    useJUnitPlatform()
    systemProperty("pact.verifier.publishResults", "true")
    systemProperty("pact.provider.version", project.version.toString())
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    include("**/*ProviderTest*")
}

dependencies {
    implementation(project(":report-generator-service:report-generator-service-api"))
    
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    // gRPC dependencies
    implementation("net.devh:grpc-spring-boot-starter:3.0.0.RELEASE")
    implementation("com.google.protobuf:protobuf-kotlin:4.32.0")
    implementation("com.google.protobuf:protobuf-java-util:4.32.0")
    implementation("io.grpc:grpc-kotlin-stub:1.4.3")
    implementation("io.grpc:grpc-stub:1.75.0")
    implementation("io.grpc:grpc-protobuf:1.75.0")
    implementation("io.grpc:grpc-netty-shaded:1.75.0")
    
    // Pact dependencies
    testImplementation("au.com.dius.pact.consumer:junit5:4.6.9")
    testImplementation("au.com.dius.pact.provider:junit5spring:4.6.9")
    testImplementation("au.com.dius.pact.provider:spring:4.6.9")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}