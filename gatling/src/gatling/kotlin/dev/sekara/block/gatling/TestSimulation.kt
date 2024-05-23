package dev.sekara.block.gatling

import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.constantUsersPerSec
import io.gatling.javaapi.core.CoreDsl.exec
import io.gatling.javaapi.core.CoreDsl.exitBlockOnFail
import io.gatling.javaapi.core.CoreDsl.rampConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.rampUsersPerSec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.OpenInjectionStep
import io.gatling.javaapi.core.OpenInjectionStep.nothingFor
import io.gatling.javaapi.core.PauseType
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status
import java.time.Duration
import java.util.UUID

const val ip = "193.164.254.220"

class TestSimulation : Simulation() {

    val getLast = exec(
        http("Get 100").get("").queryParam("limit", "100").check(status().shouldBe(200))
    )

    val getAll = exec(
        http("Get All").get("").check(status().shouldBe(200))
    )

    val ktor = http.baseUrl("http://$ip:8081/notes").contentTypeHeader("application/json").shareConnections()
    val webflux = http.baseUrl("http://$ip:8082/notes").contentTypeHeader("application/json").shareConnections()
    val mvc = http.baseUrl("http://$ip:8083/notes").contentTypeHeader("application/json").shareConnections()

    val last = scenario("Gets Last").exec(getLast)
    val all = scenario("Gets All").exec(getAll)

    init {
        setUp(
            insertConcurrent("ktor").protocols(ktor)
                .andThen(insertConcurrent("webflux").protocols(webflux))
                .andThen(insertConcurrent("mvc").protocols(mvc))
        )
    }

    private fun insert(name: String) = scenario(name).exec(
        exec(insertHttp(name))
    ).injectOpen(
        rampUsersPerSec(0.0).to(8000.0).during(30),
        nothingFor(Duration.ofSeconds(5))
    )

    private fun insertConcurrent(name: String) = scenario(name).exec(
        exec(insertHttp(name))
    ).injectClosed(
        rampConcurrentUsers(0).to(5000).during(30),
    )

    private fun insertHttp(name: String) = exitBlockOnFail().on(
        http("Insert $name").put("").body(
            StringBody(
                """
                    {"content":"Hello Gatling ${UUID.randomUUID()}"}
                """.trimIndent()
            )
        )
    )
}