package com.young_zy.mainsite.controller

import com.young_zy.mainsite.controller.exception.AuthException
import com.young_zy.mainsite.controller.exception.NotAcceptableException
import com.young_zy.mainsite.controller.exception.NotFoundException
import com.young_zy.mainsite.controller.response.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import java.io.PrintWriter
import java.io.StringWriter

@RestControllerAdvice
class ExceptionHandler {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    val Exception.stackTraceString: String
        get() {
            val stringWriter = StringWriter()
            this.printStackTrace(PrintWriter(stringWriter))
            return stringWriter.toString()
        }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<Any>{
        return ResponseEntity.status(404).body(
                Response(
                        404,
                        false,
                        e.message,
                        null
                )
        )
    }

    @ExceptionHandler(NotAcceptableException::class)
    fun handleNotAcceptableException(e: NotAcceptableException): ResponseEntity<Any>{
        return ResponseEntity.status(406).body(
                Response(
                        406,
                        false,
                        e.message,
                        null
                )
        )
    }

    @ExceptionHandler(AuthException::class)
    fun handleAuthException(e: AuthException): ResponseEntity<Any> {
        return ResponseEntity.status(403).body(
                Response(
                        403,
                        false,
                        e.message,
                        null
                )
        )
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleDefaultException(e: ResponseStatusException): ResponseEntity<Any> {
        return ResponseEntity.status(e.status)
                .headers(e.responseHeaders)
                .body(
                        Response(
                                e.status.value(),
                                false,
                                e.localizedMessage,
                                null
                        )
                )
    }

    @ExceptionHandler(Exception::class)
    fun handleOtherException(e: Exception): ResponseEntity<Any> {
        logger.error(e.stackTraceString)
        return ResponseEntity.status(500).body(
                Response(
                        500,
                        false,
                        e.message ?: "",
                        null
                )
        )
    }

}