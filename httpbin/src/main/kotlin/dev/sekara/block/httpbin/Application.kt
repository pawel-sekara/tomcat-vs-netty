package dev.sekara.block.httpbin

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.path
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.delay
import org.slf4j.event.Level

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(jacksonObjectMapper().apply {
            registerModule(JavaTimeModule())
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }))
    }
    install(CallLogging) {
        level = Level.INFO
        callIdMdc("call-id")
    }
    install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId: String ->
            callId.isNotEmpty()
        }
    }
    routing {
        get("/delay/{delay}") {
            call.parameters["delay"]?.toLong()?.let { delay(it) }
            call.respond(mapOf("hello" to "world"))
        }
    }
}
