package com.young_zy.seller.repo

import com.young_zy.seller.model.Flight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.awaitOne
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class SellerRepo {

    @Autowired private lateinit var r2dbcClient: DatabaseClient

    fun query(departureCity: String, destinationCity: String, departureTime: LocalDate): Flow<Flight> {
        return r2dbcClient.execute("SELECT * FROM `flight` WHERE DATE(`departure_time`) = :departureTime")
                .bind("departureTime", departureTime)
                .`as`(Flight::class.java)
                .fetch()
                .all()
                .asFlow()
    }

    suspend fun updateFlight(flight: Flight): Void? {
        return r2dbcClient.update()
                .table(Flight::class.java)
                .using(flight)
                .then()
                .awaitFirstOrNull()
    }

    suspend fun getFlightByFlightId(flightId: Long): Flight? {
        return r2dbcClient.select().from(Flight::class.java)
                .matching(where("flight_id").`is`(flightId))
                .fetch()
                .awaitOne()
    }

    fun findAll(): Flow<Flight> {
        return r2dbcClient.select().from(Flight::class.java)
                .fetch()
                .all()
                .asFlow()
    }


}