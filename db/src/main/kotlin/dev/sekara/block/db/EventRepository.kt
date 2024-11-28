package dev.sekara.block.db

import dev.sekara.block.db.schema.tables.records.EventsRecord
import dev.sekara.block.db.schema.tables.references.EVENTS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst

class EventRepository(
    contextHolder: JooqContextHolder
) {

    private val dsl = contextHolder.jooqContext

    fun fetchAll() = dsl.select()
        .from(EVENTS)
        .fetch { it.into(EVENTS) }

    fun fetchLast(limit: Int) = dsl.select()
        .from(EVENTS)
        .orderBy(EVENTS.CREATED_AT.desc())
        .limit(limit)
        .fetch { it.into(EVENTS) }

    fun insert(event: EventsRecord) = dsl.insertInto(EVENTS)
        .set(event)
        .returning()
        .fetchOne()

    fun fetchAllFlow() = dsl.select()
        .from(EVENTS)
        .asFlow()
        .flowOn(Dispatchers.IO)
        .map { it.into(EVENTS) }

    fun fetchLastFlow(limit: Int) = dsl.select()
        .from(EVENTS)
        .orderBy(EVENTS.CREATED_AT.desc())
        .limit(limit)
        .asFlow()
        .flowOn(Dispatchers.IO)
        .map { it.into(EVENTS) }

    suspend fun cInsert(event: EventsRecord) = dsl.insertInto(EVENTS)
        .set(event)
        .returning()
        .awaitFirst()
}