package com.young_zy.seller.controller.response

import java.time.Instant
import java.time.format.DateTimeFormatter

open class Response(open val success: Boolean = true,
                    open val reason: String = "",
                    open val timeStamp: String? = DateTimeFormatter.ISO_INSTANT.format(Instant.now()))