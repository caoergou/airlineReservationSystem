package com.young_zy.mainsite.repo

import com.young_zy.mainsite.model.AirLine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.stereotype.Repository

@Repository
class AirLineCompanyRepo {
    @Autowired private lateinit var r2dbcClient: DatabaseClient

    suspend fun findAllAirline(): Flow<AirLine> {
        return r2dbcClient.select()
                .from(AirLine::class.java)
                .fetch()
                .all()
                .asFlow()
    }

    suspend fun findAirLineById(airlineId: Long): AirLine? {
        return r2dbcClient.select()
                .from(AirLine::class.java)
                .matching(where("id").`is`(airlineId))
                .fetch()
                .one()
                .awaitFirstOrNull()
    }
}