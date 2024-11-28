package dev.sekara.block.webflux.jpa

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("events")
class JpaEvent(
    @Id
    var id: UUID?,

    var event: String,

    var data: String?,

    var createdAt: Instant?,
)
