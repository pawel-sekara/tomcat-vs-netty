package dev.sekara.block.domain.client.httpbin.blocking

import com.fasterxml.jackson.databind.ObjectMapper
import dev.sekara.block.domain.client.httpbin.HttpBinClient
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class BlockingHttpBinClient(objectMapper: ObjectMapper, customizeDispatcher: Boolean = false) :
    HttpBinClient by retrofit(objectMapper, customizeDispatcher) {

    companion object {
        fun retrofit(objectMapper: ObjectMapper, customizeDispatcher: Boolean): HttpBinClient {
            return Retrofit.Builder()
                .baseUrl("http://httpbin:8084/")
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(5.seconds.toJavaDuration())
                        .apply {
                            if (customizeDispatcher) {
                                dispatcher(Dispatcher().apply {
                                    maxRequests = 100_000
                                    maxRequestsPerHost = 100_000
                                })
                            }
                        }
                        .connectionPool(ConnectionPool(200, 5, TimeUnit.MINUTES))
                        .build()
                )
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(HttpBinClient::class.java)
        }
    }
}