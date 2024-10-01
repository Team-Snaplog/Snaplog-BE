package site.snaplog.interceptor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class LoggingFilter: WebFilter {

    private val logger: Logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        val response = exchange.response
        val requestId = request.id
        val requestUrl = request.uri.toString()
        val method = request.method.name()
        val requestTime = System.currentTimeMillis()

        logger.info("[${requestId}][Request] $method $requestUrl")

        return chain.filter(exchange)
            .contextWrite { context ->
                context
                    .put("requestId", requestId)
                    .put("requestUrl", requestUrl)
                    .put("method", method)
            }
            .doOnSuccess {
                val spendTime = System.currentTimeMillis() - requestTime
                logger.info("[${requestId}][Response] ${response.statusCode} - ${spendTime}ms")
            }
            .doOnError { error ->
                val spendTime = System.currentTimeMillis() - requestTime
                logger.error("[${requestId}][Error] ${error.message} - ${spendTime}ms")
            }
    }
}