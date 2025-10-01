import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.14.0"
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

tasks.bootJar {
    enabled = false
}

tasks.register<GenerateTask>("generateOpenApi") {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/docs/contracts/openapi-report-generator.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated")
    apiPackage.set("home.kali.report.generated.api")
    modelPackage.set("home.kali.report.generated.model")
    invokerPackage.set("home.kali.report.generated.invoker")

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
        artifact = "com.google.protobuf:protoc:4.32.0" as String
        sourceSets {
            main {
                proto {
                    srcDir("$rootDir/docs/")
                }
            }
        }
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.75.0" as String
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.3:jdk8@jar" as String
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("grpc")
                id("grpckt")
            }
            task.builtins {
                id("kotlin")
            }
        }
    }
}

sourceSets["main"].java {
    srcDir("${layout.buildDirectory.get()}/generated/src/main/kotlin")
    srcDir("${layout.buildDirectory.get()}/generated/source/proto/main/java")
    srcDir("${layout.buildDirectory.get()}/generated/source/proto/main/kotlin")
}

tasks.named("compileKotlin") {
    dependsOn("generateOpenApi", "generateProto")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.38")

    implementation("com.google.protobuf:protobuf-kotlin:4.32.0")
    implementation("io.grpc:grpc-kotlin-stub:1.4.3")
    implementation("io.grpc:grpc-stub:1.75.0")
    implementation("io.grpc:grpc-protobuf:1.75.0")
    implementation("io.grpc:grpc-netty-shaded:1.75.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}