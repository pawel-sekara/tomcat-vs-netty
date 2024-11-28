package dev.sekara.block.webflux.config

import dev.sekara.block.domain.controller.ReactiveTestController
import dev.sekara.block.domain.rest.EventDto
import dev.sekara.block.domain.rest.NoteDto
import dev.sekara.block.webflux.controller.WebfluxEventController
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.queryParamOrNull

@Configuration
class RoutingConfig {

    @Bean
    suspend fun eventRoutes(handler: WebfluxEventController) = coRouter {
        "/event-router".nest {
            GET("") { request ->
                val flow = handler.getAllEvents(request.queryParamOrNull("limit")?.toInt())
                ServerResponse.ok().bodyValueAndAwait(flow)
            }

            POST("") {
                val event = it.awaitBody<EventDto>()
                ServerResponse.ok().bodyValueAndAwait(handler.createEvent(event))
            }
        }
    }

    @Bean
    suspend fun testRoutes(controller: ReactiveTestController) = coRouter {
        "/notes".nest {
            GET("") {
                val flow =
                    it.queryParamOrNull("limit")?.toInt()?.let { controller.getLast(it) } ?: controller.getAll()
                val list = flow.toList()
                ServerResponse.ok().bodyValueAndAwait(list)
            }
            PUT("") {
                val note = it.awaitBody<NoteDto>()
                ServerResponse.ok().bodyValueAndAwait(controller.create(note))
            }
        }

        GET("/test/cpu-heavy") {
            ServerResponse.ok().bodyValueAndAwait(controller.heavy())
        }

        GET("/test/cpu-lite") {
            ServerResponse.ok().bodyValueAndAwait(controller.lite())
        }

        GET("/test/large-string") {
            ServerResponse.ok().bodyValueAndAwait(controller.largeString())
        }

        GET("/test/large-object") {
            ServerResponse.ok().bodyValueAndAwait(controller.largeObject())
        }

        GET("/test/block") {
            controller.blockingOp()
            ServerResponse.ok().buildAndAwait()
        }

        GET("/test/synchronization") {
            controller.blockingIncrement()
            ServerResponse.ok().buildAndAwait()
        }

        GET("/test/synchronization-lock") {
            controller.lockIncrement()
            ServerResponse.ok().buildAndAwait()
        }

        GET("/test/synchronization-mutex") {
            controller.mutexIncrement()
            ServerResponse.ok().buildAndAwait()
        }

        GET("/test/synchronization-context") {
            controller.contextIncrement()
            ServerResponse.ok().buildAndAwait()
        }

        GET("/test/external-call") {
            controller.externalCall()
            ServerResponse.ok().buildAndAwait()
        }

    }
}