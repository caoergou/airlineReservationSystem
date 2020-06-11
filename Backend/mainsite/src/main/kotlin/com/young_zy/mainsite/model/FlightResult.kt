package com.young_zy.mainsite.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class FlightResult(
        /**
         * 航空公司id
         */
        var airLineId: Long,
        /**
         * 航班id
         */
        var flightId: Long,
        /**
         * 航空公司名字
         */
        var airlineName: String,
        /**
         * 航班号
         */
        var number: String,
        /**
         * 出发机场
         */
        var departurePort: String,
        /**
         * 出发城市
         */
        var departureCity: String,
        /**
         * 目的地机场
         */
        var destinationPort: String,
        /**
         * 目的地城市
         */
        var destinationCity: String,
        /**
         * 出发时间
         */
        var departureTime: LocalDateTime,
        /**
         * 票价
         */
        var fare: BigDecimal,
        /**
         * 剩余机票数量
         */
        var remaining: Long
){
    constructor(flight: Flight, airLineId: Long, airlineName: String): this(
            airLineId,
            flight.flightId,
            airlineName,
            flight.number,
            flight.departurePort,
            flight.departureCity,
            flight.destinationPort,
            flight.destinationCity,
            flight.departureTime,
            flight.fare,
            flight.remaining
    )
}