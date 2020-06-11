package com.young_zy.seller.controller

import com.young_zy.seller.model.Flight
import com.young_zy.seller.service.SellerService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.time.LocalDate

@RestController
class SellerController {

    @Autowired private lateinit var sellerService: SellerService

    @GetMapping("/airline/findByRoute")
    suspend fun query(
            @RequestParam departureCity: String,
            @RequestParam destinationCity: String,
            @RequestParam departureDate: LocalDate,
            @RequestParam orderBy: String,
            @RequestParam reverse: Boolean): List<Flight> {
        val orderByList = listOf("price", "time")
        if(orderBy !in orderByList){
            throw IllegalArgumentException("orderBy not correct should be one of price or time")
        }
        val res = sellerService.query(departureCity, destinationCity, departureDate).toList()
        if (orderBy == "price"){
            res.sortedBy {
                it.fare
            }
        }else{
            res.sortedBy {
                it.departureTime
            }
        }
        return if(reverse){
            res.reversed()
        }else{
            res
        }
    }

    @GetMapping("/airline/{flightId}")
    suspend fun getFlight(@PathVariable flightId: Long): Flight? {
        return sellerService.getFlight(flightId)
    }

    @GetMapping("/airline")
    suspend fun getAllFlight(): Flow<Flight> {
        return sellerService.getAll()
    }

    @PostMapping("/airline/order/{flightId}")
    suspend fun order(@PathVariable flightId: Long): Void? {
        return sellerService.order(flightId)
    }

}