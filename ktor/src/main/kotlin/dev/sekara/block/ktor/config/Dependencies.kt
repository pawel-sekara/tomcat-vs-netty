package dev.sekara.block.ktor.config

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.sekara.block.db.EventRepository
import dev.sekara.block.db.ReactiveJooqContextHolder
import dev.sekara.block.domain.client.httpbin.reactive.ReactiveHttpBinClient
import dev.sekara.block.domain.controller.ReactiveTestController
import dev.sekara.block.domain.service.ReactiveEventService
import io.ktor.server.config.ApplicationConfig

class Dependencies(config: ApplicationConfig) {
    private val jooqContextHolder = ReactiveJooqContextHolder(
        uri = config.property("db.uri").getString(),
        user = config.property("db.user").getString(),
        password = config.property("db.password").getString(),
    )
    val jackson = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        enable(SerializationFeature.INDENT_OUTPUT)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
    private val httpBinClient = ReactiveHttpBinClient(objectMapper = jackson)
    val testController = ReactiveTestController(httpBinClient = httpBinClient)

    private val eventRepository = EventRepository(contextHolder = jooqContextHolder)
    val eventService = ReactiveEventService(repository = eventRepository, objectMapper = jackson)
}
