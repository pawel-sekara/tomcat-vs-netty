package dev.sekara.block.webflux.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dev.sekara.block.db.JooqContextHolder
import dev.sekara.block.db.NoteRepository
import dev.sekara.block.db.ReactiveJooqContextHolder
import dev.sekara.block.domain.controller.ReactiveNoteController
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
    fun noteController(noteService: NoteService): ReactiveNoteController =
        ReactiveNoteController(noteService)
}