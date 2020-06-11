package com.young_zy.mainsite.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table
data class AirLine(
        @Id
        @Column("id")
        var airlineId: Long,
        @Column("name")
        var airlineName: String,
        @Column("port")
        var url: String
)