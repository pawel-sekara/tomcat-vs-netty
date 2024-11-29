package dev.sekara.block.domain.client.httpbin.blocking

import com.fasterxml.jackson.databind.ObjectMapper
import dev.sekara.block.domain.client.httpbin.HttpBinClient
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class BlockingHttpBinClient(objectMapper: ObjectMapper) : HttpBinClient by retrofit(objectMapper) {

    companion object {
        fun retrofit(objectMapper: ObjectMapper): HttpBinClient {
            return Retrofit.Builder()
                .baseUrl("http://httpbin:8084/")
                .client(OkHttpClient.Builder()
                    .readTimeout(5.seconds.toJavaDuration())
                    .dispatcher(Dispatcher().apply {
                        maxRequests = 100000
                        maxRequestsPerHost = 100000
                    })
                    .connectionPool(ConnectionPool(100, 5, TimeUnit.MINUTES))
                    .build())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(HttpBinClient::class.java)
        }
    }
}