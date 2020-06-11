package com.young_zy.mainsite.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "url")
data class URLProperty (
        var payment: String = "localhost"
)