plugins {
    kotlin("jvm")
    id("io.gatling.gradle") version "3.11.2"
}

group = "dev.sekara.block.gatling"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    implementation("io.gatling.highcharts:gatling-charts-highcharts:3.11.2")
}

gatling {
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}