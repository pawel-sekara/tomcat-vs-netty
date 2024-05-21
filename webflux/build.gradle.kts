plugins {
    kotlin("jvm")
    id("org.springframework.boot") version "3.2.5"
    kotlin("plugin.spring") version "1.9.23"
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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:_")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:_")
    implementation("org.jetbrains.kotlin:kotlin-reflect:_")
    implementation(libs.jackson.datatype.jsr310)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:_")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}