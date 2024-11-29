package dev.sekara.block.ktor.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.logging.toLogString
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.slf4j.event.*

fun Application.configureMonitoring() {
    val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    install(CallLogging) {
        level = Level.DEBUG
        callIdMdc("call-id")
    }
    install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId: String ->
            callId.isNotEmpty()
        }
    }
    install(MicrometerMetrics) {
        registry = prometheusRegistry
    }

    routing {
        get("/metrics") {
            call.respondText(prometheusRegistry.scrape(), ContentType.Text.Plain)
        }
    }
}
