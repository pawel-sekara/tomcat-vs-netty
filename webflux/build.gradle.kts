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

    api(platform(Spring.boms.dependencies))


    implementation(Spring.boot.webflux)
    implementation(Spring.reactor.kotlin)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(KotlinX.coroutines.core)
    implementation(libs.jackson.datatype.jsr310)
    implementation(Spring.reactor.kotlin)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}