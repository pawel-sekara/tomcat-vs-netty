plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("com.google.cloud.tools.jib")
}

group = "dev.sekara.block.ktor"
version = "dev-1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":db"))
    implementation(project(":domain"))
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.serialization.kotlinx.json.jvm)
    implementation(libs.ktor.server.content.negotiation.jvm)
    implementation(libs.ktor.serialization.jackson.jvm)
    implementation(libs.ktor.server.call.logging.jvm)
    implementation(libs.ktor.server.call.id.jvm)
    implementation(libs.ktor.server.netty.jvm)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.logback.classic)
    implementation(Arrow.core)
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
        image = "ktor"
        tags = setOf("latest", version.toString())
    }
    container {
        ports = listOf("8081")
        mainClass = "io.ktor.server.netty.EngineMain"
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