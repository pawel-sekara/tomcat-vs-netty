package dev.sekara.block.gatling

import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.exec
import io.gatling.javaapi.core.CoreDsl.group
import io.gatling.javaapi.core.CoreDsl.incrementConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.incrementUsersPerSec
import io.gatling.javaapi.core.CoreDsl.pause
import io.gatling.javaapi.core.CoreDsl.repeat
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.PopulationBuilder
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.Http
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpRequestActionBuilder
import java.time.Duration
import java.util.UUID
import kotlin.random.Random.Default.nextInt
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

const val ip = "127.0.0.1"

class TestSimulation : Simulation() {

    val ktorServer = Server("ktor", ip, 8081)
    val webfluxServer = Server("webflux", ip, 8082)
    val mvcServer = Server("mvc", ip, 8083)

    fun closedScenario(scenario: Scenario) =
        concurrentScenario(mvcServer, scenario)
            .andThen(concurrentScenario(webfluxServer, scenario))
            .andThen(concurrentScenario(ktorServer, scenario))

    fun openScenario(scenario: Scenario, rpsIncrements: Double = 10.0) =
        increment(mvcServer, scenario, rpsIncrements)
            .andThen(increment(webfluxServer, scenario, rpsIncrements))
            .andThen(increment(ktorServer, scenario, rpsIncrements))

    val insert = Scenario("Insert") {
        it.post("/event").body(StringBody("{\"event\":\"Hello Gatling ${UUID.randomUUID()}\"}"))
    }
    val get = Scenario("Get lots of data") { it.get("/event?limit=${nextInt(10, 100)}") }

    val hello = Scenario("hello") { it.get("/test/hello").requestTimeout(Duration.ofSeconds(20)) }
    val work = Scenario("work") { it.get("/test/work").requestTimeout(Duration.ofSeconds(20)) }
    val call = Scenario("call") { it.get("/test/external-call").requestTimeout(Duration.ofSeconds(20)) }
    val callCustom =
        Scenario("callCustom") { it.get("/test/external-call-custom").requestTimeout(Duration.ofSeconds(20)) }
    val callReactive =
        Scenario("callReactive") { it.get("/test/external-call-reactive").requestTimeout(Duration.ofSeconds(20)) }


    init {
        setUp(
            closedScenario(hello)
                .andThen(closedScenario(work))
                .andThen(closedScenario(call))
                .andThen(concurrentScenario(mvcServer, callCustom))
                .andThen(concurrentScenario(mvcServer, callReactive))
        )
    }

    private fun concurrentScenario(
        server: Server,
        scenario: Scenario,
        users: Int = 100,
        steps: Int = 30
    ): PopulationBuilder =
        scenario("${server.name} ${scenario.name}").exec(
            group(server.name).on(scenario.action(server.name))
        ).injectClosed(
            incrementConcurrentUsers(users)
                .times(steps)
                .eachLevelLasting(20.seconds.toJavaDuration())
                .separatedByRampsLasting(10.seconds.toJavaDuration())
                .startingFrom(0)
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
            .warmUp("http://$ip:$port/event?limit=1")
    }
}