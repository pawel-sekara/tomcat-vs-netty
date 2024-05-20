package dev.sekara.block.db

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.jooq.SQLDialect
import org.jooq.impl.DSL

class JooqContextHolder(
    uri: String,
    user: String,
    password: String,
) {

    private val connectionFactory: ConnectionFactory = ConnectionFactories.get(
        ConnectionFactoryOptions.parse(
            uri
        ).mutate()
            .option(ConnectionFactoryOptions.USER, user)
            .option(ConnectionFactoryOptions.PASSWORD, password)
            .build()
    )

    val jooqCtx = DSL.using(connectionFactory, SQLDialect.POSTGRES)
}