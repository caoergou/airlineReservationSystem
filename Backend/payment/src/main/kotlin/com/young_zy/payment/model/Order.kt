package com.young_zy.payment.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.math.BigDecimal
import java.time.LocalDateTime

data class Order(
        @Id
        @Column("order_id")
        var orderId: Long? = null,
        @Column("user_id")
        var userId: Long? = null,
        @Column("flight_id")    // 银行角色不应当记录支付无关信息（指航班号和航空公司）
        var flightId: Long,
        @Column("airline_id")    // 银行角色不应当记录支付无关信息（指航班号和航空公司）
        var airLineId: Long,
        @Column("order_price")
        var orderPrice: BigDecimal,
        @Column("is_paid")
        var isPaid: Boolean,
        @Column("order_time")
        var orderTime: LocalDateTime = LocalDateTime.now()
)