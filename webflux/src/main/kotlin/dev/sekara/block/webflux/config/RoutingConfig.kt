package dev.sekara.block.webflux.config

import dev.sekara.block.domain.controller.ReactiveNoteController
import dev.sekara.block.domain.rest.NoteDto
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.queryParamOrNull

@Configuration
class RoutingConfig {

    @Bean
    suspend fun notesRoute(controller: ReactiveNoteController) = coRouter {
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
    }
}