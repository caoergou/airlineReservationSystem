package com.young_zy.mainsite.repo

import com.young_zy.mainsite.model.Order
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.awaitOne
import org.springframework.data.r2dbc.core.awaitOneOrNull
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.stereotype.Repository

@Repository
class OrderRepo {
    @Autowired
    private lateinit var r2dbcClient: DatabaseClient

    suspend fun findOrderByOrderId(orderId: Long): Order? {
        return r2dbcClient.select()
                .from(Order::class.java)
                .matching(where("order_id").`is`(orderId))
                .fetch()
                .awaitOneOrNull()
    }

    suspend fun insertOrder(order: Order): Long? {
        return r2dbcClient.insert()
                .into(Order::class.java)
                .using(order)
                .map { t ->
                    t["id"] as Long
                }
                .one()
                .awaitFirst()
    }
}