import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.14.0"
    id("au.com.dius.pact") version "4.3.10"
    id("com.google.protobuf") version "0.9.5"
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

tasks.register<GenerateTask>("generateOpenApi") {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/docs/contracts/openapi-admin-service.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated")
    apiPackage.set("home.kali.admin.generated.api")
    modelPackage.set("home.kali.admin.generated.model")
    invokerPackage.set("home.kali.admin.generated.invoker")

    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "interfaceOnly" to "true",
            "useSpringBoot3" to "true",
            "useTags" to "true"
        )
    )
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.32.0"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.75.0"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
            }
        }
    }
}

sourceSets["main"].java {
    srcDir("${layout.buildDirectory.get()}/generated/src/main/kotlin")
    srcDir("${layout.buildDirectory.get()}/generated/source/proto/main/grpc")
    srcDir("${layout.buildDirectory.get()}/generated/source/proto/main/java")
}

tasks.named("compileKotlin") {
    dependsOn("generateOpenApi")
}

pact {
    broker {
        pactBrokerUrl = "http://localhost:9292"
        pactBrokerUsername = System.getenv("PACT_BROKER_USERNAME") ?: "pact"
        pactBrokerPassword = System.getenv("PACT_BROKER_PASSWORD") ?: "password"
    }
    
    publish {
        pactBrokerUrl = "http://localhost:9292"
        version = "${project.version}"
        consumerVersion = "${project.version}"
        pactDirectory = "${layout.buildDirectory.get()}/pacts"
        tags = listOf("latest", "main")
    }
}

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

tasks.register<Test>("pactConsumerTest") {
    group = "verification"
    description = "Runs Pact consumer tests and generates contracts"
    useJUnitPlatform()
    systemProperty("pact.writer.overwrite", "true")
    systemProperty("pact.verifier.publishResults", "false")
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    include("**/*ConsumerTest*")
}

tasks.register("pactTest") {
    group = "verification"
    description = "Runs all Pact tests and publishes contracts"
    dependsOn("pactProviderTest", "pactConsumerTest", "pactPublish")

    doLast {
        logger.lifecycle("All Pact tests completed successfully!")
        logger.lifecycle("View contracts at: http://localhost:9292")
    }
}

dependencies {
    implementation(project(":report-generator-service:report-generator-service-api"))
    protobuf(files("$rootDir/docs/contracts"))

    implementation("io.grpc:grpc-netty-shaded:1.75.0")
    implementation("io.grpc:grpc-protobuf:1.75.0")
    implementation("io.grpc:grpc-stub:1.75.0")
    implementation("com.google.protobuf:protobuf-java:4.32.0")
    implementation("com.google.protobuf:protobuf-java-util:4.32.0")
    implementation("net.devh:grpc-client-spring-boot-starter:3.0.0.RELEASE")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.38")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("au.com.dius.pact.consumer:junit5:4.6.9")
    testImplementation("au.com.dius.pact.provider:junit5spring:4.6.9")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
