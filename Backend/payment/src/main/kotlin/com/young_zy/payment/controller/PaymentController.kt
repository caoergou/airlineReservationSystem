package com.young_zy.payment.controller

import com.young_zy.payment.controller.response.LoginResponse
import com.young_zy.payment.controller.response.VerifyOrderResponse
import com.young_zy.payment.service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal

@Controller
class PaymentController {

    @Autowired private lateinit var paymentService: PaymentService

    @PostMapping("/payment/login")
    suspend fun login(@RequestParam username: String, @RequestParam password: String): LoginResponse {
        return paymentService.login(username, password)
    }

    @PostMapping("/payment/new")
    suspend fun createOrder(
            @RequestParam airlineId: Long,
            @RequestParam flightId: Long,
            @RequestParam flightPrice: BigDecimal
    ): Long? {
        return paymentService.createOrder(airlineId, flightId, flightPrice)
    }

    @PostMapping("/payment/pay")
    suspend fun pay(@RequestHeader token: String, @RequestParam orderId: Long){
        return paymentService.pay(token, orderId)
    }

    @GetMapping("/payment/verifyOrder")
    suspend fun verifyOrder(@RequestParam orderId: Long): VerifyOrderResponse {
        return paymentService.verifyOrder(orderId)
    }

}