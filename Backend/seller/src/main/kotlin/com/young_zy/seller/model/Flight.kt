package com.young_zy.seller.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.math.BigDecimal
import java.time.LocalDateTime

data class Flight (
        /**
         * 航班id
         */
        @Id
        @Column("id")
        var flightId: Long,
        /**
         * 航班号
         */
        @Column("number")
        var number: String,
        /**
         * 出发机场
         */
        @Column("departure_port")
        var departurePort: String,
        /**
         * 出发城市
         */
        @Column("departure_city")
        var departureCity: String,
        /**
         * 目的地机场
         */
        @Column("destination_port")
        var destinationPort: String,
        /**
         * 目的地城市
         */
        @Column("destination_city")
        var destinationCity: String,
        /**
         * 出发时间
         */
        @Column("departure_time")
        var departureTime: LocalDateTime,
        /**
         * 票价
         */
        @Column("fare")
        var fare: BigDecimal,
        /**
         * 剩余机票数量
         */
        @Column("remaining")
        var remaining: Long
)
