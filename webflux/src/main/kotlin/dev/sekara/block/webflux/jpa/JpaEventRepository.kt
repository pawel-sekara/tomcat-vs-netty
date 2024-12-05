package dev.sekara.block.webflux.jpa

import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

@Repository
interface JpaEventRepository: R2dbcRepository<SpringDataEvent, UUID> {

    @Modifying
    @Query("INSERT INTO events (id, event, data, created_at) VALUES (:id, :event, :data, :createdAt)")
    fun insert(id: UUID, event: String, data: String?, createdAt: Instant): Mono<SpringDataEvent>
}

