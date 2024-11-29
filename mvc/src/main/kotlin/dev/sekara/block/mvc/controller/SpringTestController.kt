package dev.sekara.block.mvc.controller

import dev.sekara.block.domain.controller.BlockingTestController
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SpringTestController(
    @Qualifier("blocking") private val defaultDispatcher: BlockingTestController,
    @Qualifier("custom") private val customDispatcher: BlockingTestController,
    @Qualifier("reactive") private val testControllerWithReactiveClient: BlockingTestController,
) {

    @GetMapping("/test/hello")
    fun helloWorld(): String {
        return "Hello, World!"
    }

    @GetMapping("/test/hello")
    suspend fun work(): ResponseEntity<Unit> {
        delay(100)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/test/external-call")
    suspend fun externalCall() {
        defaultDispatcher.externalCall()
    }

    @GetMapping("/test/external-call-custom")
    suspend fun externalCallCustom() {
        customDispatcher.externalCall()
    }

    @GetMapping("/test/external-call-reactive")
    suspend fun externalCallReactive() {
        testControllerWithReactiveClient.externalCall()
    }

}