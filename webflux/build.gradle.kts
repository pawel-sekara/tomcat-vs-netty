plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    kotlin("plugin.spring")
    id("com.google.cloud.tools.jib")
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
//    implementation(Spring.boot.data.jdbc) // Uncomment this line and comment the above line to switch to JDBC
    implementation(Spring.boot.data.r2dbc)
    implementation("org.hibernate.reactive:hibernate-reactive-core:_")
    implementation(libs.postgresql)
    implementation(libs.r2dbc.postgresql)
    implementation("io.micrometer:micrometer-registry-prometheus")

    implementation(Arrow.core)
    implementation(Arrow.fx.coroutines)
    implementation(Spring.boot.webflux)
    implementation(Spring.boot.actuator)
    implementation(Spring.reactor.kotlin)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(KotlinX.coroutines.core)
    implementation(KotlinX.coroutines.reactor)
    implementation(KotlinX.coroutines.slf4j)
    implementation(libs.jackson.datatype.jsr310)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
jib {
    from {
        // Temurin has been chosen for its TCK compliance, switching from Temurin to any JDK is seamless
        image = "${project.properties["baseImageRepoUri"]?.let { "$it/" } ?: ""}amazoncorretto:17"
        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "webflux"
        tags = setOf("latest", version.toString())
    }
    container {
        ports = listOf("8082")
        mainClass = "dev.sekara.block.webflux.WebfluxApplicationKt"
        jvmFlags = listOf(
            "-server",
            "-XX:InitialRAMPercentage=25",
            "-XX:MaxRAMPercentage=75",
            "-XX:+UseG1GC",
            "-XX:MaxGCPauseMillis=100",
            "-XX:+UseStringDeduplication",
            "-Daws.crt.log.destination=STDOUT",
        )
    }
}