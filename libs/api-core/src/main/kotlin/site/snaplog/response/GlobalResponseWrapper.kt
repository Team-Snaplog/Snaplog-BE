package site.snaplog.response

import org.springframework.http.codec.HttpMessageWriter
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException

class GlobalResponseWrapper(
    messageWriters: List<HttpMessageWriter<*>>,
    contentTypeResolver: RequestedContentTypeResolver
) : ResponseBodyResultHandler(messageWriters, contentTypeResolver) {

    override fun supports(result: HandlerResult): Boolean {
        if (result.returnTypeSource.declaringClass.name == "springfox.documentation.swagger.web.ApiResourceController") {
            return false
        }

        return result.returnTypeSource.declaringClass.isAnnotationPresent(RequestBody::class.java)
    }

    override fun handleResult(exchange: ServerWebExchange, result: HandlerResult): Mono<Void> {
        val body = when (val value = result.returnValue) {
            is Mono<*> -> value
            is Flux<*> -> value.collectList()
            else -> throw SnaplogException(StatusCode.INTERNAL_SERVER_ERROR, "반환 타입은 Mono 또는 Flux만 지원합니다.")
        }.map {
            SnaplogResponse(
                status = 200,
                message = "Success",
                data = it
            )
        }

        val returnTypeSource = result.returnTypeSource

        return writeBody(body, returnTypeSource, exchange)
    }
}