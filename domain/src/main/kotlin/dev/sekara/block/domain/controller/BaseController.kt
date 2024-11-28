package dev.sekara.block.domain.controller

import dev.sekara.block.domain.client.httpbin.HttpBinClient
import kotlin.random.Random

open class BaseController(
    private val httpBinClient: HttpBinClient
) {

    @Volatile
    protected var counter: Long = 0

    suspend fun externalCall(): Map<String, Any> {
        return Random.nextLong(0, 100).let {
            httpBinClient.networkCall(it)
        }
    }
}