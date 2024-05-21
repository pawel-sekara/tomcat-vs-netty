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
    implementation("org.springframework.boot:spring-boot-starter-webflux:_")
    implementation(Spring.reactor.kotlin)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.jackson.datatype.jsr310)
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:_")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}