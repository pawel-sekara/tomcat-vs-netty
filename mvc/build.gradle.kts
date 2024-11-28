plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    kotlin("plugin.spring")
    id("com.google.cloud.tools.jib")
}

group = "dev.sekara.block.mvc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":db"))
    implementation(project(":domain"))

    api(platform(Spring.boms.dependencies))

    implementation(Spring.boot.web)
    implementation(KotlinX.coroutines.core)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.jackson.datatype.jsr310)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

jib {
    from {
        // Temurin has been chosen for its TCK compliance, switching from Temurin to any JDK is seamless
        image = "${project.properties["baseImageRepoUri"]?.let { "$it/" } ?: ""}amazoncorretto:17"
        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "mvc"
        tags = setOf("latest", version.toString())
    }
    container {
        ports = listOf("8083")
        mainClass = "dev.sekara.block.mvc.MvcApplicationKt"
        jvmFlags = listOf(
            "-server",
            "-XX:InitialRAMPercentage=25",
            "-XX:MaxRAMPercentage=75",
            "-XX:+UseG1GC",
            "-XX:MaxGCPauseMillis=100",
            "-XX:+UseStringDeduplication",
            "-Daws.crt.log.destination=STDOUT",
        )
    }
}