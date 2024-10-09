package site.snaplog.exception

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import site.snaplog.enums.StatusCode
import site.snaplog.response.SnaplogErrorResponse
import site.snaplog.util.consts.ColorCode

@Component
@Order(-2)
class GlobalExceptionHandler: ErrorWebExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    private val objectMapper: ObjectMapper = ObjectMapper()

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val response = exchange.response
        val request = exchange.request
        val requestId = request.id

        val errorResponse = when (ex) {
            is SnaplogException -> SnaplogErrorResponse(ex.statusCode.code, ex.statusCode.message, ex.message)
            else -> SnaplogErrorResponse(StatusCode.INTERNAL_SERVER_ERROR.code, StatusCode.INTERNAL_SERVER_ERROR.message)
        }

        response.statusCode = HttpStatus.valueOf(errorResponse.status)
        response.headers.contentType = MediaType.APPLICATION_JSON

        logger.error("${ColorCode.GREEN}[${requestId}]${ColorCode.RED}[Error]${ColorCode.RESET} ${errorResponse.status} ${errorResponse.message} ${errorResponse.errorDetail ?: ""}${ColorCode.RESET}\n${ex.stackTraceToString()}")

        val dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse))
        return response.writeWith(Mono.just(dataBuffer))
    }
}