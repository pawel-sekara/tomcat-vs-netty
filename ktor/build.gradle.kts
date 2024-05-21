plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
}

group = "dev.sekara.block.ktor"
version = "1.0-SNAPSHOT"

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