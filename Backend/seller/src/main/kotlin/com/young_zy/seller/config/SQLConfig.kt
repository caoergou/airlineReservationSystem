package com.young_zy.seller.config

import com.young_zy.seller.config.property.SQLProperties
import io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.reactive.TransactionalOperator

@Configuration
@EnableR2dbcRepositories
@EnableTransactionManagement
class SQLConfig(@Autowired private final val sqlProperties: SQLProperties) : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return ConnectionFactories.get(
                builder()
                        .option(DRIVER, "pool")
                        .option(PROTOCOL, "mysql")
                        .option(HOST, sqlProperties.host)
                        .option(USER, sqlProperties.username)
                        .option(PASSWORD, sqlProperties.password)
                        .option(MAX_SIZE, sqlProperties.maxConnection)
                        .option(DATABASE, sqlProperties.database)
                        .build()
        )
    }

    @Bean
    fun transactionManager(
            @Qualifier("connectionFactory")
            connectionFactory: ConnectionFactory
    ): R2dbcTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }

    @Bean
    fun transactionOperator(transactionManager: R2dbcTransactionManager): TransactionalOperator {
        return TransactionalOperator.create(transactionManager)
    }

    @Bean
    fun r2dbcDatabaseClient(
            @Qualifier("connectionFactory")
            connectionFactory: ConnectionFactory
    ): DatabaseClient {
        return DatabaseClient.create(connectionFactory)
    }

    override fun getCustomConverters(): MutableList<Any> {
        val converterList = mutableListOf<Any>()
        return converterList
    }
}