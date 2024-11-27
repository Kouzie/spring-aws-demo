import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

allprojects {
    group = "com.aws.demo"
    version = "0.0.1-SNAPSHOT"
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-validation")

        // implementation(platform("aws.sdk.kotlin:bom:1.3.82"))
        implementation(platform("software.amazon.awssdk:bom:2.21.20"))
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.thedeanda:lorem:2.2") // For general use

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation(kotlin("test"))
    }

    tasks.test {
        useJUnitPlatform()
    }
    kotlin {
        jvmToolchain(17)
    }
}
dependencies {
    implementation(kotlin("reflect"))
}