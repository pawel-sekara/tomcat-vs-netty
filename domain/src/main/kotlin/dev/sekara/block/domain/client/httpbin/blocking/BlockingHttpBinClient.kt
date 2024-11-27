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

class BlockingHttpBinClient(objectMapper: ObjectMapper) : HttpBinClient by retrofit(objectMapper) {

    companion object {
        fun retrofit(objectMapper: ObjectMapper): HttpBinClient {
            return Retrofit.Builder()
                .baseUrl("http://127.0.0.1:8084/")
                .client(OkHttpClient.Builder()
                    .build())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(HttpBinClient::class.java)
        }
    }
}