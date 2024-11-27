package dev.sekara.block.domain.controller

import dev.sekara.block.domain.client.httpbin.HttpBinClient
import dev.sekara.block.domain.rest.NoteDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.locks.ReentrantLock
import kotlin.random.Random
import kotlin.random.Random.Default

open class BaseController(
    private val httpBinClient: HttpBinClient
) {

    @Volatile
    protected var counter: Long = 0

    private val lock = ReentrantLock()

    fun lite(): Int {
        return (1..100000 + ThreadLocalRandom.current().nextInt(1000))
            .reduce { acc, i -> acc + i }
    }

    fun heavy(): Int {
        return (1..5000000 + ThreadLocalRandom.current().nextInt(1000))
            .reduce { acc, i -> acc + i }
    }

    fun largeString(): String = largeJson

    fun largeObject(): List<NoteDto> = largeObject

    fun blockingOp() {
        Thread.sleep(1000)
    }

    fun lockIncrement() {
        lock.lock()
        runBlocking { delay(1) }
        counter++
        lock.unlock()
    }

    fun blockingIncrement() {
        synchronized(this) {
            runBlocking { delay(1) }
            counter++
        }
    }

    suspend fun externalCall(): Map<String, Any> {
        return Random.nextLong(0, 100).let {
            httpBinClient.networkCall(it)
        }
    }

    companion object {
        private val row = "{\"key\":\"value\"},"
        private val largeJson = "[${
            row
                .repeat(5000)
                .trim(',')
        }]"
        private val largeObject = (1..50000)
            .map { NoteDto(UUID.randomUUID(), "Content $it", null) }

    }
}