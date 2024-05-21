plugins {
    kotlin("jvm")
    id("io.ktor.plugin") version "2.3.11"
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
    implementation("io.ktor:ktor-server-core-jvm:2.3.11")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.11")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.11")
    implementation("io.ktor:ktor-serialization-jackson-jvm:2.3.11")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.11")
    implementation("io.ktor:ktor-server-call-id-jvm:2.3.11")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.11")
    implementation("io.ktor:ktor-server-config-yaml:2.3.11")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("io.arrow-kt:arrow-core:1.2.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:_")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}