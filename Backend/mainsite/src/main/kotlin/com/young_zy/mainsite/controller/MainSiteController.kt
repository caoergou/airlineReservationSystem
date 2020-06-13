package com.young_zy.mainsite.controller

import com.young_zy.mainsite.controller.response.OrderResponse
import com.young_zy.mainsite.model.FlightResult
import com.young_zy.mainsite.service.MainSiteService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class MainSiteController {

    @Autowired private lateinit var mainSiteService: MainSiteService

    @ExperimentalCoroutinesApi
    @GetMapping("/flightReservation/findByPeriodRoute")
    suspend fun findByRoute(@RequestParam departure: String,
                            @RequestParam destination: String,
                            @RequestParam departureDate: List<LocalDate>,
                            @RequestParam orderBy: String?,
                            @RequestParam reverse: Boolean?
    ): List<FlightResult> {
        return mainSiteService.findByRoute(departure, destination, departureDate, orderBy ?: "time", reverse ?: false)
    }

    @PostMapping("/flightReservation/order")
    suspend fun order(@RequestParam airlineId: Long, @RequestParam flightId: Long): OrderResponse {
        return mainSiteService.order(airlineId, flightId)
    }

    @PostMapping("/flightReservation/payment")
    suspend fun paymentNotify(@RequestParam orderId: Long){
        return mainSiteService.paymentNotify(orderId)
    }

}