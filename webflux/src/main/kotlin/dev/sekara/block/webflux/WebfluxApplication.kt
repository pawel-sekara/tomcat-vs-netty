package dev.sekara.block.webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [R2dbcAutoConfiguration::class]
)
class WebfluxApplication

fun main(args: Array<String>) {
	runApplication<WebfluxApplication>(*args)
}
