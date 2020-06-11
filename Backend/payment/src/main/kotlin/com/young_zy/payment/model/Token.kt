package com.young_zy.payment.model

data class Token(
        var token: String = "",
        val uid: Long = -1,
        var username: String = ""
)