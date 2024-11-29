package dev.sekara.block.webflux.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.NotAcceptableStatusException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebInputException

@ControllerAdvice
class WebfluxExceptionHandler {

    private val logger = LoggerFactory.getLogger(WebfluxExceptionHandler::class.java)

    @ExceptionHandler(RuntimeException::class)
    fun handleException(ex: RuntimeException, request: ServerWebExchange): ResponseStatusException {
        logger.error("Error processing request: ${request.request}", ex)

        return ResponseStatusException(HttpStatus.BAD_REQUEST ,"An error occurred while processing the request")
    }
}