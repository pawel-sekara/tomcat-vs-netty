import de.fayard.refreshVersions.core.StabilityLevel

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
    id("de.fayard.refreshVersions") version "0.60.5"
}
rootProject.name = "blocking-vs-nonblocking"

@Suppress("UnstableApiUsage")
refreshVersions {
    rejectVersionIf {
        candidate.stabilityLevel != StabilityLevel.Stable
    }
}
include("domain")
include("db")
include("ktor")
include("webflux")
include("mvc")
