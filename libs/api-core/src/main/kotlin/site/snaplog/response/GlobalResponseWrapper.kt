package site.snaplog.response

import org.springframework.http.MediaType
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class GlobalResponseWrapper(
    messageWriters: List<HttpMessageWriter<*>>,
    contentTypeResolver: RequestedContentTypeResolver
) : ResponseBodyResultHandler(messageWriters, contentTypeResolver) {

    override fun supports(result: HandlerResult): Boolean {
        val className = result.returnTypeSource.declaringClass.name
        if (className.startsWith("springfox.documentation.") ||
            className.startsWith("org.springdoc.")) {
            return false
        }

        return result.returnTypeSource.declaringClass.run {
            isAnnotationPresent(RestController::class.java) || isAnnotationPresent(ResponseBody::class.java)
        }
    }

    override fun handleResult(exchange: ServerWebExchange, result: HandlerResult): Mono<Void> {
        return when (val value = result.returnValue) {
            is Mono<*> -> value.flatMap { Mono.justOrEmpty(it) }
            is Flux<*> -> value.collectList()
            null -> Mono.empty()
            else -> Mono.just(value)
        }.map {
            SnaplogResponse(
                status = 200,
                message = "Success",
                data = it
            )
        }.defaultIfEmpty(
            SnaplogResponse(
                status = 200,
                message = "Success",
                data = null
            )
        ).flatMap {
            writeBody(it, result.returnTypeSource, exchange)
        }
    }
}