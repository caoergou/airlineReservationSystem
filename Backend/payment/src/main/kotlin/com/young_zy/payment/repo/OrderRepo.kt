package com.young_zy.payment.repo

import com.young_zy.payment.model.Order
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.*
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.stereotype.Repository

@Repository
class OrderRepo {
    @Autowired
    private lateinit var r2dbcDatabaseClient: DatabaseClient

    suspend fun getOrderByOrderId(orderId: Long): Order? {
        return r2dbcDatabaseClient.select()
                .from(Order::class.java)
                .matching(where("order_id").`is`(orderId))
                .fetch()
                .awaitOneOrNull()
    }

    suspend fun updateOrder(order: Order): Void? {
        return r2dbcDatabaseClient.update()
                .table(Order::class.java)
                .using(order)
                .then()
                .awaitFirstOrNull()
    }

    suspend fun insertOrder(order: Order): Long {
        return r2dbcDatabaseClient.insert()
                .into(Order::class.java)
                .using(order)
                .map { t ->
                    t["id"] as Long
                }
                .one()
                .awaitFirst()
    }
}