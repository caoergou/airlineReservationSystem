package com.young_zy.seller.service

import com.young_zy.seller.controller.exception.NotAcceptableException
import com.young_zy.seller.controller.exception.NotFoundException
import com.young_zy.seller.model.Flight
import com.young_zy.seller.repo.SellerRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import java.time.LocalDate

@Service
class SellerService{

    @Autowired private lateinit var sellerRepo: SellerRepo

    @Autowired private lateinit var transactionalOperator: TransactionalOperator

    suspend fun query(departureCity: String, destinationCity: String, departureTime: LocalDate): Flow<Flight> {
        return sellerRepo.query(departureCity, destinationCity, departureTime)
    }

    suspend fun getFlight(flightId: Long): Flight? {
        return sellerRepo.getFlightByFlightId(flightId) ?: throw NotFoundException("flight with flightId $flightId not found")
    }

    suspend fun order(flightId: Long): Void? {
        return transactionalOperator.executeAndAwait {
            val flight = sellerRepo.getFlightByFlightId(flightId) ?: throw NotFoundException()
            if (flight.remaining > 0){
                flight.remaining--
                sellerRepo.updateFlight(flight)
            }else{
                throw NotAcceptableException("flight with ${flight.flightId} has sold out")
            }
        }
    }

    suspend fun getAll(): List<Flight> {
        return sellerRepo.findAll().toList()
    }

    suspend fun cancelOrder(flightId: Long){
        transactionalOperator.executeAndAwait {
            val flight = sellerRepo.getFlightByFlightId(flightId) ?: throw NotFoundException("flight with flightId $flightId not found")
            flight.remaining++
            sellerRepo.updateFlight(flight)
        }
    }

}