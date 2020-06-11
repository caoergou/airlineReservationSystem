package com.young_zy.payment.repo

import com.young_zy.payment.model.User
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.*
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.stereotype.Repository

@Repository
class UserRepo {

    @Autowired private lateinit var r2dbcDatabaseClient: DatabaseClient

    suspend fun getUserByUsername(username: String): User? {
        return r2dbcDatabaseClient.select()
                .from(User::class.java)
                .matching(where("username").`is`(username))
                .fetch()
                .awaitOneOrNull()
    }

    suspend fun getUserByUid(userId: Long): User? {
        return r2dbcDatabaseClient.select()
                .from(User::class.java)
                .matching(where("id").`is`(userId))
                .fetch()
                .awaitOneOrNull()
    }

    suspend fun updateUser(user: User): Void? {
        return r2dbcDatabaseClient.update()
                .table(User::class.java)
                .using(user)
                .then()
                .awaitFirstOrNull()
    }
}