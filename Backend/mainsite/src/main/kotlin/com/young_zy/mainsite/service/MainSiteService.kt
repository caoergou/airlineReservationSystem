package com.young_zy.mainsite.service

import com.young_zy.mainsite.config.property.URLProperty
import com.young_zy.mainsite.controller.exception.NotAcceptableException
import com.young_zy.mainsite.controller.exception.NotFoundException
import com.young_zy.mainsite.controller.response.OrderResponse
import com.young_zy.mainsite.model.Flight
import com.young_zy.mainsite.model.FlightResult
import com.young_zy.mainsite.model.Order
import com.young_zy.mainsite.model.PaymentVerifyResponseBody
import com.young_zy.mainsite.repo.AirLineCompanyRepo
import com.young_zy.mainsite.repo.OrderRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import org.springframework.web.reactive.function.client.*
import java.time.LocalDate

@Service
class MainSiteService {

    @Autowired private lateinit var airLineCompanyRepo: AirLineCompanyRepo

    @Autowired private lateinit var urlProperty: URLProperty

    @Autowired private lateinit var orderRepo: OrderRepo

    @Autowired private lateinit var transactionalOperator: TransactionalOperator

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
                val webClient = WebClient.builder().baseUrl(it.url).build()
                val uri = "/airline/findByRoute?departureCity=${departureCity}&destinationCity=${destinationCity}&departureDate=${date}&orderBy=${orderBy}&reverse=${reverse}"
                val reqTemp = webClient
                        .get()
                        .uri(uri)
                        .awaitExchange()
                val temp = reqTemp
                        .bodyToFlow<Flight>()
                        .map { flight ->
                            FlightResult(flight, it.airlineId!!, it.airlineName)
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
        var uri = "/airline/$flightId"
        val airlineWebClient = WebClient.builder().baseUrl(airline.url).build()
        val response = airlineWebClient.get().uri(uri).awaitExchange()
        val flight = response.bodyToMono(Flight::class.java).awaitFirst()
        if(flight.remaining <= 0){
            throw NotAcceptableException("flight $flightId of airline $airlineId sold out")
        }
        //create order
        uri = "/payment/new?airlineId=${airlineId}&flightId=${flightId}&flightPrice=${flight.fare}"
        val paymentWebClient = WebClient.builder().baseUrl(urlProperty.payment).build()
        val req = paymentWebClient.post().uri(uri).awaitExchange()
        val orderId = req.bodyToMono(Long::class.java).awaitFirst()
        uri = "/airline/order/$flightId"
        val res = airlineWebClient.post().uri(uri).awaitExchange()
        if(res.rawStatusCode() == 406){
            throw NotAcceptableException("flight has been sold out")
        }
        if(res.rawStatusCode() == 404){
            throw NotFoundException("flight not found")
        }
        transactionalOperator.executeAndAwait {
            orderRepo.insertOrder(Order(null, orderId, airlineId, flightId))
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
        var uri = "/payment/verifyOrder?orderId=${orderId}"
        val paymentWebClient = WebClient.builder().baseUrl(urlProperty.payment).build()
        val response = paymentWebClient.get().uri(uri).awaitExchange()
        if (response.rawStatusCode() == 404){
            throw NotFoundException("order with orderId $orderId not found")
        }
        val responseBody = response.awaitBody<PaymentVerifyResponseBody>()
        if(!responseBody.isPaid){
            val order = orderRepo.findOrderByOrderId(orderId) ?: throw NotFoundException("order with orderId $orderId not found")
            val airline = airLineCompanyRepo.findAirLineById(order.airlineId) ?: throw NotFoundException("airline with airlineId ${order.airlineId} not found")
            val airlineWebClient = WebClient.builder().baseUrl(airline.url).build()
            uri = "/airline/cancel?flightId=${order.flightId}"
            airlineWebClient.post().uri(uri).awaitExchange()
            throw NotAcceptableException("order with orderId $orderId not paid")
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun getAll(): List<FlightResult> {
        val airLineCompanies = airLineCompanyRepo.findAllAirline()
        var resFlow = flowOf<FlightResult>()
        airLineCompanies.collect{
            val uri = "/airline"
            val webClient = WebClient.builder().baseUrl(it.url).build()
            val reqTemp = webClient
                    .get()
                    .uri(uri)
                    .awaitExchange()
            val temp = reqTemp
                    .bodyToFlow<Flight>()
                    .map { flight ->
                        FlightResult(flight, it.airlineId!!, it.airlineName)
                    }
            resFlow = merge(resFlow, temp)
        }
        return resFlow.toList()
    }

}