package dev.sekara.block.ktor.config

import dev.sekara.block.db.NoteRepository
import dev.sekara.block.db.ReactiveJooqContextHolder
import io.ktor.server.config.ApplicationConfig
import dev.sekara.block.domain.controller.ReactiveTestController
import dev.sekara.block.domain.service.NoteService

class Dependencies(config: ApplicationConfig) {
    private val jooqContextHolder = ReactiveJooqContextHolder(
        uri = config.property("db.uri").getString(),
        user = config.property("db.user").getString(),
        password = config.property("db.password").getString(),
    )
    private val repository = NoteRepository(contextHolder = jooqContextHolder)
    private val noteService = NoteService(repository = repository)
    val testController = ReactiveTestController(noteService = noteService)
}
