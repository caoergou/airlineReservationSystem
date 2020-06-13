package com.young_zy.mainsite.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("order_info")
data class Order (
        @Id
        @Column("id")
        val id: Long?,
        @Column("order_id")
        val orderId: Long,
        @Column("airline_id")
        val airlineId: Long,
        @Column("flight_id")
        val flightId: Long
)