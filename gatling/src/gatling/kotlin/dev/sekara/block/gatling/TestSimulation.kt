package dev.sekara.block.gatling

import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.group
import io.gatling.javaapi.core.CoreDsl.incrementConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.incrementUsersPerSec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.PopulationBuilder
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.Http
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpRequestActionBuilder
import java.time.Duration
import java.util.UUID

const val ip = "127.0.0.1"

class TestSimulation : Simulation() {

    val ktorServer = Server("ktor", ip, 8081)
    val webfluxServer = Server("webflux", ip, 8082)
    val mvcServer = Server("mvc", ip, 8083)

    fun closedScenario(scenario: Scenario) =
        concurrentScenario(ktorServer, scenario)
            .andThen(concurrentScenario(webfluxServer, scenario))
            .andThen(concurrentScenario(mvcServer, scenario))

    fun openScenario(scenario: Scenario, rpsIncrements: Double = 10.0) =
        increment(mvcServer, scenario, rpsIncrements)
            .andThen(increment(webfluxServer, scenario, rpsIncrements))
            .andThen(increment(ktorServer, scenario, rpsIncrements))

    val insert = Scenario("Insert") {
        it.put("/notes").body(StringBody("{\"content\":\"Hello Gatling ${UUID.randomUUID()}\"}"))
    }
    val last = Scenario("query") { it.get("/notes?limit=10") }
    val cpuIntensive = Scenario("Cpu intensive") { it.get("/test/cpu-heavy") }
    val cpuLight = Scenario("lite") { it.get("/test/cpu-lite") }
    val largeString = Scenario("largeString") { it.get("/test/large-string") }
    val largeObject = Scenario("largeObject") { it.get("/test/large-object") }
    val blocking = Scenario("blocking") { it.get("/test/block") }
    val synchronized = Scenario("synchronized") { it.get("/test/synchronization") }
    val lock = Scenario("synchronized-lock") { it.get("/test/synchronization-lock") }
    val mutex = Scenario("mutex") { it.get("/test/synchronization-mutex") }
    val context = Scenario("context") { it.get("/test/synchronization-context") }
    val call = Scenario("call") { it.get("/test/external-call").requestTimeout(Duration.ofSeconds(10)) }
    val syncCall = Scenario("call") { it.get("/test/external-call-2").requestTimeout(Duration.ofSeconds(10)) }

    init {
        setUp(
//                openScenario(insert)
//                .andThen(openScenario(cpuLight))
//                .andThen(openScenario(largeString, 25.0))
//                .andThen(openScenario(blocking, 2.0))
//                .andThen(openScenario(largeObject, 50.0))
//                .andThen(openScenario(cpuIntensive, 2.0))
            closedScenario(call)
                .andThen(concurrentScenario(mvcServer, syncCall))
//            concurrentScenario(mvcServer, call)
//                .andThen(openScenario(lock, 1500.0))
//                .andThen(openScenario(mutex, 150.0))
//                .andThen(openScenario(context, 1500.0))
//                .andThen(openScenario(last))

        )
    }

    private fun concurrentScenario(server: Server, scenario: Scenario): PopulationBuilder =
        scenario("Closed ${server.name} ${scenario.name}").exec(
            group(server.name).on(
                scenario.action("Closed ${server.name}")
            ).exitHereIfFailed()
        ).injectClosed(
//            constantConcurrentUsers(10)
//                .during(Duration.ofSeconds(10))
            incrementConcurrentUsers(35)
                .times(10)
                .eachLevelLasting(Duration.ofMillis(2000))
                .startingFrom(10)
        ).protocols(server.protocol)

    private fun increment(server: Server, scenario: Scenario, rpsIncrements: Double = 50.0) =
        scenario("Open ${server.name} ${scenario.name}").exec(
            group(server.name).on(
                scenario.action("Open ${server.name}")
            )
        ).exitHereIfFailed()
            .injectOpen(
                incrementUsersPerSec(rpsIncrements)
                    .times(6)
                    .eachLevelLasting(5)
                    .separatedByRampsLasting(5)
                    .startingFrom(rpsIncrements)
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