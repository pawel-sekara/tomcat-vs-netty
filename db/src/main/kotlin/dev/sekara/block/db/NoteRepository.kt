package dev.sekara.block.db

import dev.sekara.block.db.schema.tables.records.NotesRecord
import dev.sekara.block.db.schema.tables.references.NOTES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst

class NoteRepository(
    contextHolder: JooqContextHolder,
) {
    private val dsl = contextHolder.jooqContext

    fun cFetchAll(): Flow<NotesRecord> =
        dsl.select()
            .from(NOTES)
            .asFlow()
            .flowOn(Dispatchers.IO)
            .map { it.into(NOTES) }

    fun fetchAll(): List<NotesRecord> = dsl.select()
        .from(NOTES)
        .fetch { it.into(NOTES) }

    fun cFetchLast(limit: Int): Flow<NotesRecord> =
        dsl.select()
            .from(NOTES)
            .orderBy(NOTES.CREATED_AT.desc())
            .limit(limit)
            .asFlow()
            .map { it.into(NOTES) }

    fun fetchLast(limit: Int): List<NotesRecord> = dsl.select()
        .from(NOTES)
        .orderBy(NOTES.CREATED_AT.desc())
        .limit(limit)
        .fetch { it.into(NOTES) }

    suspend fun cInsert(note: NotesRecord): NotesRecord? = dsl.insertInto(NOTES)
        .set(note)
        .returning()
        .awaitFirst()

    fun insert(note: NotesRecord): NotesRecord? = dsl.insertInto(NOTES)
        .set(note)
        .returning()
        .fetchAny()
}