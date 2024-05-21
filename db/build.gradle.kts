import org.jooq.meta.jaxb.Property

plugins {
    kotlin("jvm")
    id("org.flywaydb.flyway")
    id("org.jooq.jooq-codegen-gradle")
}

group = "dev.sekara.block.db"
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
    implementation(libs.jooq.kotlin)
    implementation(libs.jooq.kotlin.coroutines)
    implementation(libs.r2dbc.postgresql)
    jooqCodegen(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.tomcat.jdbc)
}

flyway {
    url = "jdbc:postgresql://127.0.0.1:5432/ktor"
    user = "root"
    password = "root"
    driver = "org.postgresql.Driver"
    loggers = arrayOf("slf4j")
}

val projectDir = project.projectDir.toString().trimEnd { it == '/' }
val jooqGenerationTargetDir = "$projectDir/build/generated-sources/jooq"
sourceSets["main"].java.srcDir(jooqGenerationTargetDir)

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