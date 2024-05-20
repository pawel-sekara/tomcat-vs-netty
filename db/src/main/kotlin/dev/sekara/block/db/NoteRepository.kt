package dev.sekara.block.db

import dev.sekara.block.db.schema.tables.records.NotesRecord
import dev.sekara.block.db.schema.tables.references.NOTES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class NoteRepository(
    contextHolder: JooqContextHolder,
) {
    private val dsl = contextHolder.jooqCtx
    private val dispatcher = Dispatchers.IO.limitedParallelism(4)

    fun cFetchAll(): Flow<NotesRecord> = dsl.select()
        .from(NOTES)
        .asFlow()
        .map { it.into(NOTES) }

    fun fetchAll(): List<NotesRecord> = blocking {
        cFetchAll().toList()
    }

    fun cFetchLast(limit: Int) = dsl.select()
        .from(NOTES)
        .orderBy(NOTES.CREATED_AT.desc())
        .limit(limit)
        .asFlow()

    fun fetchLast(limit: Int) = blocking { cFetchLast(limit) }

    suspend fun cInsert(note: NotesRecord): Int = dsl.insertInto(NOTES)
        .set(note)
        .awaitFirst()

    fun insert(note: NotesRecord) = blocking {
        cInsert(note)
    }

    private fun <T> blocking(invocation: suspend () -> T): T = runBlocking(dispatcher) { invocation() }
}