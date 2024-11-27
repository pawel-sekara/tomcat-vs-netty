package dev.sekara.block.mvc.controller

import dev.sekara.block.domain.controller.BlockingTestController
import dev.sekara.block.domain.rest.NoteDto
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SpringTestController(
    private val testController: BlockingTestController
) {

    @GetMapping("/notes")
    fun getLastNotes(@RequestParam(required = false) limit: Int? = null): List<NoteDto> {
        val notes = limit?.let { testController.getLast(it) } ?: testController.getAll()
        return notes
    }

    @PutMapping("/notes")
    fun createNote(@RequestBody note: NoteDto): NoteDto {
        return testController.create(note)
    }

    @GetMapping("/test/cpu-heavy")
    fun cpuHeavyOperation(): Int {
        return testController.heavy()
    }

    @GetMapping("/test/cpu-lite")
    fun cpuLiteOperation(): Int {
        return testController.lite()
    }

    @GetMapping("/test/large-string")
    fun largeString(): String {
        return testController.largeString()
    }

    @GetMapping("/test/large-object")
    fun largeObject(): List<NoteDto> {
        return testController.largeObject()
    }

    @GetMapping("/test/block")
    fun blockingOperation() {
        testController.blockingOp()
    }

    @GetMapping("/test/synchronization")
    fun blockingSynchronization() {
        testController.blockingIncrement()
    }

    @GetMapping("/test/synchronization-lock")
    fun blockingSynchronizationLock() {
        testController.lockIncrement()
    }

    @GetMapping("/test/synchronization-mutex")
    fun mutexSynchronization() {
        // Not applicable
    }

    @GetMapping("/test/synchronization-context")
    fun contextSynchronization() {
        // Not applicable
    }

    @GetMapping("/test/external-call")
    suspend fun externalCall() {
        testController.externalCall()
    }

    @GetMapping("/test/external-call-2")
    fun externalCall2() = runBlocking {
        testController.externalCall()
    }


}