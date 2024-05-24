package dev.sekara.block.gatling

import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.group
import io.gatling.javaapi.core.CoreDsl.incrementConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.incrementUsersPerSec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.PopulationBuilder
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.Http
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpRequestActionBuilder
import java.util.UUID

const val ip = "127.0.0.1"

class TestSimulation : Simulation() {

    val ktorServer = Server("ktor", ip, 8081)
    val webfluxServer = Server("webflux", ip, 8082)
    val mvcServer = Server("mvc", ip, 8083)

    fun closedScenario(scenario: Scenario) =
        incrementConcurrent(ktorServer, scenario)
            .andThen(incrementConcurrent(webfluxServer, scenario))
            .andThen(incrementConcurrent(mvcServer, scenario))

    fun openScenario(scenario: Scenario) =
        increment(ktorServer, scenario)
            .andThen(increment(webfluxServer, scenario))
            .andThen(increment(mvcServer, scenario))

    val insert = Scenario("Insert") {
        it.put("/notes").body(StringBody("{\"content\":\"Hello Gatling ${UUID.randomUUID()}\"}"))
    }
    val cpuIntensive = Scenario("Cpu intensive") { it.get("/test/cpu-heavy") }
    val cpuLight = Scenario("lite") { it.get("/test/cpu-lite") }
    val largeString = Scenario("largeString") { it.get("/test/large-string") }
    val largeObject = Scenario("largeObject") { it.get("/test/large-object") }

    init {
        setUp(
            closedScenario(insert)
                .andThen(closedScenario(cpuLight))
                .andThen(closedScenario(largeString))
                .andThen(closedScenario(largeObject))
                .andThen(closedScenario(cpuIntensive))
                // open scenarios
                .andThen(openScenario(insert))
                .andThen(openScenario(cpuLight))
                .andThen(openScenario(largeString))
//                .andThen(openScenario(largeObject))
//                .andThen(openScenario(cpuIntensive))
        )
    }

    private fun incrementConcurrent(server: Server, scenario: Scenario): PopulationBuilder =
        scenario("Closed ${server.name} ${scenario.name}").exec(
            group(server.name).on(
                scenario.action("Closed ${server.name}")
            )
        ).injectClosed(
            incrementConcurrentUsers(200)
                .times(10)
                .eachLevelLasting(5)
                .startingFrom(200)
        ).protocols(server.protocol)

    private fun increment(server: Server, scenario: Scenario) =
        scenario("Open ${server.name} ${scenario.name}").exec(
            group(server.name).on(
                scenario.action("Open ${server.name}")
            )
        ).injectOpen(
            incrementUsersPerSec(850.0)
                .times(10)
                .eachLevelLasting(5)
                .startingFrom(850.0)
        ).protocols(server.protocol)

    data class Scenario(
        val name: String,
        val call: (Http) -> HttpRequestActionBuilder,
    ) {
        val action: (String) -> HttpRequestActionBuilder = { call(http("$name $it")) }
    }

    data class Server(
        val name: String,
        val ip: String,
        val port: Int,
    ) {
        val protocol = http.baseUrl("http://$ip:$port")
            .contentTypeHeader("application/json")
            .shareConnections()
            .warmUp("http://$ip:$port/notes?limit=1")
    }
}