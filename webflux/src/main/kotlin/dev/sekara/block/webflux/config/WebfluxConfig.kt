package dev.sekara.block.webflux.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.util.MimeTypeUtils.APPLICATION_JSON
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class WebfluxConfig { // : WebFluxConfigurer {

//    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
//        configurer.defaultCodecs().jackson2JsonDecoder(jackson2JsonDecoder(jacksonObjectMapper()))
//        configurer.defaultCodecs().jackson2JsonEncoder(jackson2JsonEncoder(jacksonObjectMapper()))
//    }

    @Bean
    @Primary
    fun jacksonObjectMapper(): ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .apply {
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }

    @Bean
    fun jackson2JsonEncoder(objectMapper: ObjectMapper): Jackson2JsonEncoder =
        Jackson2JsonEncoder(objectMapper, APPLICATION_JSON)

    @Bean
    fun jackson2JsonDecoder(objectMapper: ObjectMapper): Jackson2JsonDecoder =
        Jackson2JsonDecoder(objectMapper, APPLICATION_JSON)
}