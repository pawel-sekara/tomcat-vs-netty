package dev.sekara.block.gatling

import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.exec
import io.gatling.javaapi.core.CoreDsl.rampConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status
import java.util.UUID

class TestSimulation : Simulation() {

    val getLast = exec(
        http("Get 100").get("").queryParam("limit", "100").check(status().shouldBe(200))
    )

    val getAll = exec(
        http("Get All").get("").check(status().shouldBe(200))
    )

    val ktor = http.baseUrl("http://192.168.1.2:8081/notes").contentTypeHeader("application/json").shareConnections()
    val webflux = http.baseUrl("http://192.168.1.2:8082/notes").contentTypeHeader("application/json").shareConnections()
    val mvc = http.baseUrl("http://192.168.1.2:8083/notes").contentTypeHeader("application/json").shareConnections()

    val last = scenario("Gets Last").exec(getLast)
    val all = scenario("Gets All").exec(getAll)

    init {
        setUp(
//            last.injectClosed(constantConcurrentUsers(750).during(30))
//            insert.injectClosed(constantConcurrentUsers(750).during(30)),
            insertConcurrent("ktor concurrent").protocols(ktor)
                .andThen(insertConcurrent("webflux concurrent").protocols(webflux))
                .andThen(insertConcurrent("mvc concurrent").protocols(mvc))
//            last.injectOpen(rampUsersPerSec(0.0).to(1000.0).during(60)),
//            all.injectOpen(constantUsersPerSec(0.2).during(60)),
        )
    }

    private fun insertConcurrent(name: String) = scenario(name).exec(
        exec(insertHttp(name))
    ).injectClosed(rampConcurrentUsers(0).to(4000).during(30))

    private fun insertHttp(name: String) = http("Insert $name").put("").body(
        StringBody(
            """
                    {"content":"Hello Gatling ${UUID.randomUUID()}"}
                """.trimIndent()
        )
    ).check(status().shouldBe(200))
}