package dev.sekara.block.mvc.controller

import dev.sekara.block.domain.controller.BlockingTestController
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

    @GetMapping("/test/external-call")
    suspend fun externalCall() {
        testController.externalCall()
    }

    @GetMapping("/test/external-call-2")
    fun externalCall2() = runBlocking {
        testController.externalCall()
    }


}