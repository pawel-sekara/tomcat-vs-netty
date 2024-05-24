package dev.sekara.block.webflux.config

import dev.sekara.block.db.JooqContextHolder
import dev.sekara.block.db.NoteRepository
import dev.sekara.block.db.ReactiveJooqContextHolder
import dev.sekara.block.domain.controller.ReactiveTestController
import dev.sekara.block.domain.service.NoteService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AppConfig {

    @Bean
    fun jooqContextHolder(
        @Value("\${db.uri}") uri: String,
        @Value("\${db.user}") user: String,
        @Value("\${db.password}") password: String,
    ): ReactiveJooqContextHolder = ReactiveJooqContextHolder(
        uri, user, password
    )

    @Bean
    fun noteRepository(contextHolder: JooqContextHolder): NoteRepository =
        NoteRepository(contextHolder)

    @Bean
    fun noteService(noteRepository: NoteRepository): NoteService =
        NoteService(noteRepository)

    @Bean
    fun noteController(noteService: NoteService): ReactiveTestController =
        ReactiveTestController(noteService)
}