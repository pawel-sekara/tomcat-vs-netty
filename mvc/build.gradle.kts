plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    kotlin("plugin.spring")
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