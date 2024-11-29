package dev.sekara.block.ktor.plugins

import dev.sekara.block.ktor.config.Dependencies
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.delay

fun Application.addTestRoutes(dependencies: Dependencies) {
    routing {
        val controller = dependencies.testController

        get("/test/external-call") {
            controller.externalCall()
            call.respond(HttpStatusCode.OK)
        }

        get("/test/hello") {
            call.respond(HttpStatusCode.OK, "Hello, World!")
        }

        get("/test/work") {
            delay(100)
            call.respond(HttpStatusCode.OK)
        }
    }
}
