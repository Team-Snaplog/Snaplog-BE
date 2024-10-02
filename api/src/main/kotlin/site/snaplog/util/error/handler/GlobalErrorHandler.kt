package site.snaplog.util.error.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import site.snaplog.util.consts.ColorCode
import site.snaplog.util.enums.StatusCode
import site.snaplog.util.error.SnaplogError

@Component
@Order(-2)
class GlobalErrorHandler: ErrorWebExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(GlobalErrorHandler::class.java)
    private val objectMapper: ObjectMapper = ObjectMapper()

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val response = exchange.response
        val request = exchange.request
        val requestId = request.id
        val requestTime = exchange.getAttribute<Long>("requestTime") ?: System.currentTimeMillis()
        val spendTime = System.currentTimeMillis() - requestTime

        val errorResponse = when (ex) {
            is SnaplogError -> ErrorResponse(ex.statusCode.code, ex.statusCode.message, ex.message)
            else -> ErrorResponse(StatusCode.INTERNAL_SERVER_ERROR.code, StatusCode.INTERNAL_SERVER_ERROR.message)
        }

        response.statusCode = HttpStatus.valueOf(errorResponse.status)
        response.headers.contentType = MediaType.APPLICATION_JSON

        logger.error("${ColorCode.GREEN}[${requestId}]${ColorCode.RED}[Error]${ColorCode.RESET} ${errorResponse.status} ${errorResponse.message} ${errorResponse.detailMessage ?: ""} ${ColorCode.YELLOW}- ${spendTime}ms${ColorCode.RESET}")

        val dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse))
        return response.writeWith(Mono.just(dataBuffer))
    }
}

data class ErrorResponse(val status: Int, val message: String, val detailMessage: String? = null)
