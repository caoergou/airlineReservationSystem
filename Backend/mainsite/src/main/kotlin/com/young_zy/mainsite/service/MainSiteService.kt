package com.young_zy.mainsite.service

import com.young_zy.mainsite.controller.exception.NotAcceptableException
import com.young_zy.mainsite.controller.exception.NotFoundException
import com.young_zy.mainsite.controller.response.OrderResponse
import com.young_zy.mainsite.model.Flight
import com.young_zy.mainsite.model.FlightResult
import com.young_zy.mainsite.repo.AirLineCompanyRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.reactive.function.client.bodyToFlow
import java.time.LocalDate

@Service
class MainSiteService {

    @Autowired private lateinit var airLineCompanyRepo: AirLineCompanyRepo

    @Autowired private lateinit var webClient: WebClient

    @ExperimentalCoroutinesApi
    suspend fun findByRoute(
            departureCity: String,
            destinationCity: String,
            departureDate: List<LocalDate>,
            orderBy: String,
            reverse: Boolean
    ): List<FlightResult> {
        val airLineCompanies = airLineCompanyRepo.findAllAirline()
        var resFlow = flowOf<FlightResult>()
        airLineCompanies.collect {
            departureDate.forEach { date ->
                val uri = "${it.url}/airline/findByRoute?departureCity=${departureCity}&destinationCity=${destinationCity}&departureDate=${date}&orderBy=${orderBy}&reverse=${reverse}"
                val reqTemp = webClient
                        .get()
                        .uri(uri)
                        .awaitExchange()
                val temp = reqTemp
                        .bodyToFlow<Flight>()
                        .map { flight ->
                            FlightResult(flight, it.airlineId, it.airlineName)
                        }
                resFlow = merge(resFlow, temp)
            }
        }
        val res = resFlow.toList()
        if(orderBy == "time"){
            res.sortedBy {
                it.departureTime
            }
        }else{
            res.sortedBy {
                it.fare
            }
        }
        return if(reverse){
            res.reversed()
        }else{
            res
        }
    }

    @Throws(NotAcceptableException::class, NotFoundException::class)
    suspend fun order(airlineId: Long, flightId: Long): OrderResponse {
        val airline = airLineCompanyRepo.findAirLineById(airlineId) ?: throw NotFoundException("airline with id $airlineId not found")
        var uri = "${airline.url}/$flightId"
        val flight = webClient.get().uri(uri).awaitExchange().bodyToMono(Flight::class.java).awaitFirst()
        if(flight.remaining <= 0){
            throw NotAcceptableException("flight $flightId of airline $airlineId sold out")
        }
        //create order
        uri = "/payment/new?airlineId=${airlineId}&flightId=${flightId}&price=${flight.fare}"
        val req = webClient.post().uri(uri).awaitExchange()
        val orderId = req.bodyToMono(Long::class.java).awaitFirst()
        uri = "${airline.url}/order/$flightId"
        val res = webClient.post().uri(uri).awaitExchange()
        if(res.statusCode().value() == 404){
            throw NotFoundException("flight not found")
        }
        return OrderResponse(
                orderId,
                0,
                false,
                flightId,
                flight.fare
        )
    }

    suspend fun paymentNotify(orderId: Long){
        val uri = ""
    }

}