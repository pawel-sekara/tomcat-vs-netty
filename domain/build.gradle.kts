plugins {
    kotlin("jvm")
}

group = "dev.sekara.block.domain"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":db"))
    implementation(libs.jooq)
    implementation(KotlinX.coroutines.core)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}