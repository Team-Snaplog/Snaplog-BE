package site.snaplog.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import site.snaplog.util.ColorCode

@Component
class LoggingFilter: WebFilter {

    private val logger: Logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        val response = exchange.response
        val requestId = request.id
        val requestUrl = request.uri.path
        val method = request.method.name()
        val requestTime = System.currentTimeMillis()

        logger.info("${ColorCode.GREEN}[${requestId}]${ColorCode.CYAN}[Request]${ColorCode.RESET} $method $requestUrl")

        return chain.filter(exchange)
            .contextWrite { context ->
                context
                    .put("requestId", requestId)
                    .put("requestUrl", requestUrl)
                    .put("method", method)
            }
            .doOnSuccess {
                val spendTime = System.currentTimeMillis() - requestTime
                logger.info("${ColorCode.GREEN}[${requestId}]${ColorCode.BLUE}[Response]${ColorCode.RESET} ${response.statusCode} ${ColorCode.YELLOW}- ${spendTime}ms${ColorCode.RESET}")
            }
    }
}