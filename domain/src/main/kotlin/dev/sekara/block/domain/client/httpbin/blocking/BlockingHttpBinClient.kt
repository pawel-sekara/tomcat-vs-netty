package dev.sekara.block.domain.client.httpbin.blocking

import com.fasterxml.jackson.databind.ObjectMapper
import dev.sekara.block.domain.client.httpbin.HttpBinClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class BlockingHttpBinClient(objectMapper: ObjectMapper) : HttpBinClient by retrofit(objectMapper) {

    companion object {
        fun retrofit(objectMapper: ObjectMapper): HttpBinClient {
            return Retrofit.Builder()
                .baseUrl("http://127.0.0.1/")
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(HttpBinClient::class.java)
        }
    }
}