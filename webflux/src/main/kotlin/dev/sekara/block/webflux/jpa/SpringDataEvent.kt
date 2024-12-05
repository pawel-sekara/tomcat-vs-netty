package dev.sekara.block.webflux.jpa

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("events")
data class SpringDataEvent(
    @Id
    var id: UUID? = null,
    var event: String,
    var data: String? = null,
    var createdAt: Instant? = null,
)

