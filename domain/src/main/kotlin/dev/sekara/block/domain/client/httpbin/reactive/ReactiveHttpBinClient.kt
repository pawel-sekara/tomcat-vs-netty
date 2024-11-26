package dev.sekara.block.domain.client.httpbin.reactive


import com.fasterxml.jackson.databind.ObjectMapper
import dev.sekara.block.domain.client.httpbin.HttpBinClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.JacksonConverter

class ReactiveHttpBinClient(objectMapper: ObjectMapper) : HttpBinClient {

    private val ktor = ktor(objectMapper)

    override suspend fun networkCall(delay: Double): Map<String, Any> {
        return ktor.post("http://127.0.0.1/delay/$delay") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    companion object {
        fun ktor(objectMapper: ObjectMapper): HttpClient {
            return HttpClient(CIO) {
                install(ContentNegotiation) {
                    register(ContentType.Application.Json, JacksonConverter(objectMapper))
                }
            }
        }
    }
}