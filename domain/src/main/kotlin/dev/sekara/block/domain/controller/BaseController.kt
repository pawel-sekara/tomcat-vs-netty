package dev.sekara.block.domain.controller

import dev.sekara.block.domain.rest.NoteDto
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom

open class BaseController {

    @Volatile
    protected var counter: Long = 0

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

    fun blockingIncrement() {
        synchronized(this) {
            counter++
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