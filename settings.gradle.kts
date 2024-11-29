pluginManagement {
    val kotlinJvmVersion: String by settings
    val kotlinSpringVersion: String by settings
    val springBootVersion: String by settings
    val dependencyManagementVersion: String by settings
    val springCloudAwsVersion: String by settings
    val softwareAwsSdkBomVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinJvmVersion
        kotlin("plugin.spring") version kotlinSpringVersion
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version dependencyManagementVersion
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "spring-aws-demo"

include(
    "aws-credential",
    "dynamodb-demo",
    "s3-demo",
    "sqs-demo",
    "lambda-demo",
    "systemmanager-demo",
)