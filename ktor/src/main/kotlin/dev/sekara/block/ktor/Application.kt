package dev.sekara.block.ktor

import dev.sekara.block.ktor.config.Dependencies
import dev.sekara.block.ktor.plugins.configureMonitoring
import dev.sekara.block.ktor.plugins.configureRouting
import dev.sekara.block.ktor.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val dependencies = Dependencies(environment.config)
    configureSerialization(dependencies)
    configureMonitoring()
    configureRouting(dependencies)
}
