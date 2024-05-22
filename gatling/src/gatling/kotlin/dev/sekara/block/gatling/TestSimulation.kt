package dev.sekara.block.gatling

import io.gatling.core.body.BodyWithStringExpression
import io.gatling.core.body.StringBody
import io.gatling.javaapi.core.Body
import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.CoreDsl.exec
import io.gatling.javaapi.core.CoreDsl.rampUsers
import io.gatling.javaapi.core.CoreDsl.rampUsersPerSec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status
import kotlin.random.Random

class TestSimulation : Simulation() {

    val getAll = exec(
        http("Get all").get("").check(status().shouldBe(200))
    )

    val insert = exec(
        http("Insert").put("")
            .body(CoreDsl.StringBody("""
            {
                "content": "Hello Gatling! ${Random.nextLong()}"
            }
            """.trimIndent())).check(status().shouldBe(200))
    )

    val httpProtocol = http.baseUrl("http://127.0.0.1:8081/notes")
        .contentTypeHeader("application/json")

    val gets = scenario("Gets").exec(getAll)
    val inserts = scenario("Inserts").exec(insert)

    init {
        setUp(
            gets.injectOpen(rampUsersPerSec(100.0).to(200.0).during(60)),
            inserts.injectOpen(rampUsersPerSec(10.0).to(100.0).during(40))
        ).protocols(httpProtocol)
    }
}