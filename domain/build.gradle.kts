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
    implementation(Square.Retrofit2.retrofit)
    implementation(Square.Retrofit2.converter.jackson)
    implementation(Square.OkHttp3.okHttp)
    implementation(Ktor.client.core)
    implementation(Ktor.client.cio)
    implementation(Ktor.client.java)
    implementation(Ktor.client.apache)
    implementation(Ktor.client.contentNegotiation)
    implementation(libs.jackson.datatype.jsr310)
    implementation(Ktor.plugins.serialization.jackson)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}