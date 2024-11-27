package dev.sekara.block.domain.client.httpbin.reactive


import com.fasterxml.jackson.databind.ObjectMapper
import dev.sekara.block.domain.client.httpbin.HttpBinClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.apache.Apache
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.util.logging.KtorSimpleLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class ReactiveHttpBinClient(objectMapper: ObjectMapper) : HttpBinClient {

    private val ktor = ktor(objectMapper)

    override suspend fun networkCall(delay: Long): Map<String, Any> {
        return try {
            ktor.get("http://httpbin:8084/delay/$delay") {
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            KtorSimpleLogger("Error").error("Error calling httpbin", e)
            emptyMap()
        }
    }

    companion object {
        fun ktor(objectMapper: ObjectMapper): HttpClient {
            return HttpClient(Java) {
                engine {
                    config {
                        executor(Dispatchers.IO.asExecutor())
                    }
                }
                install(ContentNegotiation) {
                    register(ContentType.Application.Json, JacksonConverter(objectMapper))
                }
            }
        }
    }
}