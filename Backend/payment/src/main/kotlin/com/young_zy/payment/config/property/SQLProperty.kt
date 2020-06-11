package com.young_zy.payment.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "sql")
data class SQLProperties(
        var host: String = "localhost",
        var username: String = "user",
        var password: String = "",
        var database: String = "question_box",
        var maxConnection: Int = 30
)