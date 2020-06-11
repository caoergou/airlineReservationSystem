package com.young_zy.mainsite.controller.response

import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderResponse (
        val id: Long,
        var userId: Long,
        var isPaid: Boolean,
        var flightId: Long,
        var orderPrice: BigDecimal,
        var orderTime: LocalDateTime = LocalDateTime.now()
)