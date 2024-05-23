package dev.sekara.block.db

import org.apache.tomcat.jdbc.pool.DataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL

class BlockingJooqContextHolder(
    uri: String,
    user: String,
    password: String,
    driver: String,
    connectionInitialSize: Int = 20,
    connectionMaxActive: Int = 100,
): JooqContextHolder {

    private val dataSource: javax.sql.DataSource = DataSource().apply {
        this.driverClassName = driver
        this.url = uri
        this.username = user
        this.password = password
        this.initialSize = connectionInitialSize
        this.maxActive = connectionMaxActive
        this.validationQuery = "SELECT 1;"
        this.isTestOnBorrow = true
        this.validationInterval = 0
    }

    override val jooqContext: DSLContext = DSL.using(dataSource, SQLDialect.POSTGRES)
}