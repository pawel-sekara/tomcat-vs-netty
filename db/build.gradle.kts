import org.jooq.meta.jaxb.Property

plugins {
    kotlin("jvm")
    id("org.flywaydb.flyway")
    id("org.jooq.jooq-codegen-gradle")
}

group = "dev.sekara.block"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

buildscript {
    dependencies {
        classpath(libs.flyway.database.postgresql)
        classpath(libs.jooq.meta.extensions)
        classpath(libs.jooq.codegen)
    }
}

dependencies {
    implementation(libs.postgresql)
    implementation(libs.logback.classic)
    implementation(libs.jooq)
    runtimeOnly(libs.r2dbc.postgresql)
    jooqCodegen(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
}

flyway {
    url = "jdbc:postgresql://127.0.0.1:5432/ktor"
    user = "root"
    password = "root"
    driver = "org.postgresql.Driver"
    loggers = arrayOf("slf4j")
}

jooq {
    configuration {
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                properties = listOf(
                    Property()
                        .withKey("scripts")
                        .withValue("$projectDir/src/main/resources/db/migration/*.sql"),
                    Property()
                        .withKey("sort")
                        .withValue("alphanumeric"),
                    Property()
                        .withKey("defaultNameCase")
                        .withValue("lower"),
                )
            }
            generate {
                isDeprecationOnUnknownTypes = false
                isJavaTimeTypes = true
                isKotlinNotNullRecordAttributes = true
            }
            target {
                packageName = "dev.sekara.block.db.schema"
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}