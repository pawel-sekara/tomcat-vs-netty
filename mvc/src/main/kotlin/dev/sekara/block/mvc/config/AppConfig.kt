package dev.sekara.block.mvc.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.sekara.block.db.BlockingJooqContextHolder
import dev.sekara.block.db.EventRepository
import dev.sekara.block.db.JooqContextHolder
import dev.sekara.block.domain.client.httpbin.blocking.BlockingHttpBinClient
import dev.sekara.block.domain.client.httpbin.reactive.ReactiveHttpBinClient
import dev.sekara.block.domain.controller.BlockingTestController
import dev.sekara.block.domain.service.BlockingEventService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@EnableWebMvc
class AppConfig : WebMvcConfigurer {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(MappingJackson2HttpMessageConverter(objectMapper()))
    }

    @Bean
    fun jooqContextHolder(
        @Value("\${db.uri}") uri: String,
        @Value("\${db.user}") user: String,
        @Value("\${db.password}") password: String,
    ): BlockingJooqContextHolder = BlockingJooqContextHolder(
        uri, user, password, "org.postgresql.Driver"
    )
    @Bean
    fun testController(): BlockingTestController =
        BlockingTestController(reactiveHttpBinClient())

    @Bean
    fun blockingHttpBinClient() = BlockingHttpBinClient(objectMapper())

    @Bean
    fun reactiveHttpBinClient() = ReactiveHttpBinClient(objectMapper())

    @Bean
    fun eventRepository(contextHolder: JooqContextHolder) = EventRepository(contextHolder)

    @Bean
    fun eventsService(eventRepository: EventRepository) = BlockingEventService(eventRepository, objectMapper())

    @Bean
    fun objectMapper() = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .apply {
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
}