plugins {
    kotlin("jvm") version "2.0.0" apply false
    kotlin("plugin.spring") version "2.0.0" apply false
    id("org.springframework.boot") version "3.5.3" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    kotlin("plugin.jpa") version "2.0.0" apply false
    id("com.google.protobuf") version "0.9.5" apply false
    id("build.buf") version "0.10.2" apply false
}

allprojects {
    group = "home.kali"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
