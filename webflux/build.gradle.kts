plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

group = "dev.sekara.block.webflux"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":db"))
    implementation(project(":domain"))
    implementation(Spring.boot.webflux)
    implementation(libs.jackson.module.kotlin)
    implementation(Spring.reactor.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.jackson.datatype.jsr310)
    implementation(KotlinX.coroutines.reactor)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}