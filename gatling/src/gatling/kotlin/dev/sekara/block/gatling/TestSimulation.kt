package dev.sekara.block.gatling

import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.constantUsersPerSec
import io.gatling.javaapi.core.CoreDsl.exec
import io.gatling.javaapi.core.CoreDsl.rampUsersPerSec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status

class TestSimulation : Simulation() {

    val getLast = exec(
        http("Get 100").get("").queryParam("limit", "100").check(status().shouldBe(200))
    )

    val getAll = exec(
        http("Get All").get("").check(status().shouldBe(200))
    )

    val httpProtocol = http.baseUrl("http://127.0.0.1:8080/notes")
        .contentTypeHeader("application/json")
        .shareConnections()

    val last = scenario("Gets Last").exec(getLast)
    val all = scenario("Gets All").exec(getAll)

    init {
        setUp(
            last.injectClosed(constantConcurrentUsers(750).during(30))
//            last.injectOpen(rampUsersPerSec(0.0).to(1000.0).during(60)),
//            all.injectOpen(constantUsersPerSec(0.2).during(60)),
        ).protocols(httpProtocol)
    }
}