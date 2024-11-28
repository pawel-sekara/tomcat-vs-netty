package dev.sekara.block.webflux.config

import dev.sekara.block.domain.controller.ReactiveTestController
import dev.sekara.block.domain.rest.EventDto
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
        GET("/test/external-call") {
            controller.externalCall()
            ServerResponse.ok().buildAndAwait()
        }

    }
}