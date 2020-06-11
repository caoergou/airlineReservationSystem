package com.young_zy.payment.service

import com.young_zy.payment.controller.exception.NotAcceptableException
import com.young_zy.payment.controller.exception.NotFoundException
import com.young_zy.payment.controller.exception.UnauthorizedException
import com.young_zy.payment.controller.response.LoginResponse
import com.young_zy.payment.controller.response.VerifyOrderResponse
import com.young_zy.payment.model.Order
import com.young_zy.payment.model.Token
import com.young_zy.payment.repo.OrderRepo
import com.young_zy.payment.repo.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import java.math.BigDecimal
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Service
class PaymentService {

    @Autowired private lateinit var userRepo: UserRepo

    @Autowired private lateinit var orderRepo: OrderRepo

    @Autowired private lateinit var tokenRedisTemplate: RedisTemplate<String, Token>

    @Autowired private lateinit var transactionalOperator: TransactionalOperator

    suspend fun login(username: String, password: String): LoginResponse {
        val user = userRepo.getUserByUsername(username) ?: throw NotAcceptableException("user with username $username not found")
        if (!PasswordHash.validatePassword(password, user.hashedPassword)){
            throw NotAcceptableException("password not correct")
        }
        val random = SecureRandom()
        val bytes = ByteArray(20)
        random.nextBytes(bytes)
        val longToken = abs(random.nextLong())
        val tokenStr = longToken.toString(16)
        val token = Token(
                "$username:$tokenStr",
                user.userId!!,
                user.username
        )
        tokenRedisTemplate.opsForValue().set(token.token, token, 1, TimeUnit.DAYS)
        return LoginResponse(token.token, user.balance)
    }

    suspend fun pay(token: String, orderId: Long){
        val tokenObj = tokenRedisTemplate.opsForValue().get(token) ?: throw UnauthorizedException("not logged in or token expired")
        tokenRedisTemplate.expire(token, 1, TimeUnit.DAYS)
        transactionalOperator.executeAndAwait {
            val order = orderRepo.getOrderByOrderId(orderId) ?: throw NotFoundException("order with orderId $orderId not found")
            val user = userRepo.getUserByUid(tokenObj.uid) ?: throw NotFoundException("user with uid ${tokenObj.uid} not found")
            if(order.isPaid){
                throw NotAcceptableException("order with orderId $orderId has already been paid")
            }
            if(user.balance < order.orderPrice){
                throw NotAcceptableException("user haven't got enough balance")
            }
            user.balance -= order.orderPrice
            order.userId = user.userId
            order.isPaid = true
            userRepo.updateUser(user)
            orderRepo.updateOrder(order)
        }
    }

    suspend fun createOrder(airLineId: Long, flightId: Long, flightPrice: BigDecimal): Long? {
        return transactionalOperator.executeAndAwait {
            val order = Order(
                    null,
                    null,
                    flightId,
                    airLineId,
                    flightPrice,
                    false
            )
            return@executeAndAwait orderRepo.insertOrder(order)
        }
    }

    suspend fun verifyOrder(orderId: Long): VerifyOrderResponse {
        val order = orderRepo.getOrderByOrderId(orderId) ?: throw NotFoundException("order with orderId $orderId not found")
        return VerifyOrderResponse(order.isPaid)
    }

}