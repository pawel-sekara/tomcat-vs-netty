package dev.sekara.block.webflux.config

import com.fasterxml.jackson.databind.ObjectMapper
import dev.sekara.block.db.EventRepository
import dev.sekara.block.db.JooqContextHolder
import dev.sekara.block.db.ReactiveJooqContextHolder
import dev.sekara.block.domain.client.httpbin.reactive.ReactiveHttpBinClient
import dev.sekara.block.domain.controller.ReactiveTestController
import dev.sekara.block.domain.service.ReactiveEventService
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
    fun testController(httpBinClient: ReactiveHttpBinClient): ReactiveTestController =
        ReactiveTestController(httpBinClient)

    @Bean
    fun httpBinClient(objectMapper: ObjectMapper) = ReactiveHttpBinClient(objectMapper)

    @Bean
    fun eventRepository(contextHolder: JooqContextHolder) = EventRepository(contextHolder)

    @Bean
    fun eventsService(eventRepository: EventRepository, objectMapper: ObjectMapper) =
        ReactiveEventService(eventRepository, objectMapper)
}