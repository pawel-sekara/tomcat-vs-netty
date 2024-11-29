package dev.sekara.block.db

import io.r2dbc.pool.PoolingConnectionFactoryProvider
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL

class ReactiveJooqContextHolder(
    uri: String,
    user: String,
    password: String,
): JooqContextHolder {

    private val connectionFactory: ConnectionFactory = ConnectionFactories.get(
        ConnectionFactoryOptions.parse(
            uri
        ).mutate()
            .option(ConnectionFactoryOptions.USER, user)
            .option(ConnectionFactoryOptions.PASSWORD, password)
            .option(PoolingConnectionFactoryProvider.INITIAL_SIZE, 20)
            .option(PoolingConnectionFactoryProvider.MAX_SIZE, 100)
            .build()
    )

    override val jooqContext: DSLContext = DSL.using(connectionFactory, SQLDialect.POSTGRES)
}