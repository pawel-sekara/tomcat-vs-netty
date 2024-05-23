plugins {
    kotlin("jvm")
    id("io.gatling.gradle")
}

group = "dev.sekara.block.gatling"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    implementation(libs.gatling.charts.highcharts)
}

gatling {
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}