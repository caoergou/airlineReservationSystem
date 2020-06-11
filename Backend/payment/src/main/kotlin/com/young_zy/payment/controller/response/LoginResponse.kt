package com.young_zy.payment.controller.response

import java.math.BigDecimal

data class LoginResponse(
        val token: String,
        val balance: BigDecimal
)