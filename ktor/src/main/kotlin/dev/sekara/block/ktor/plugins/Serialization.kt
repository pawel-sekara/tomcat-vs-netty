package dev.sekara.block.ktor.plugins

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import dev.sekara.block.ktor.config.Dependencies
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization(dependencies: Dependencies) {
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(dependencies.jackson))
    }
    routing {
        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
