package dev.sekara.block.db

import org.jooq.DSLContext

interface JooqContextHolder {
    val jooqContext: DSLContext
}