package dev.sekara.block.domain.client.httpbin

import retrofit2.http.GET
import retrofit2.http.Path


interface HttpBinClient {

    @GET("delay/{delay}")
    suspend fun networkCall(@Path("delay") delay: Double): Map<String, Any>
}