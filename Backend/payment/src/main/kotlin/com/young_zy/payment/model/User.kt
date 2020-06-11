package com.young_zy.payment.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.math.BigDecimal

data class User(
        @Id
        @Column("id")
        var userId: Long?,
        @Column("username")
        var username: String,
        @Column("hashed_password")
        var hashedPassword: String,
        @Column("balance")
        var balance: BigDecimal
)