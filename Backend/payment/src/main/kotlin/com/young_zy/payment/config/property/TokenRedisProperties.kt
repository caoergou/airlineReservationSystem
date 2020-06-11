package com.young_zy.payment.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "token.redis")
data class TokenRedisProperties(
        var host: String = "localhost",
        var port: Int = 6379,
        var dbIndex: Int = 0,
        var password: String = ""
)