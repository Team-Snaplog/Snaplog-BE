package site.snaplog.security.resolver

import org.springframework.core.MethodParameter
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException

@Component
class AccessTokenArgumentResolver: HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AccessToken::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any?> {
        return if (parameter.parameterType == String::class.java) {
            val token = exchange.request.headers.getFirst("Authorization")?.substringAfter("Bearer ")
            if (token == null) {
                Mono.error(SnaplogException(StatusCode.UNAUTHORIZED, "토큰이 존재하지 않습니다."))
            } else {
                ReactiveSecurityContextHolder.getContext()
                    .map { token }
            }
        } else {
            Mono.error(SnaplogException(StatusCode.INTERNAL_SERVER_ERROR, "@AccessToken 어노테이션은 MemberEntity 타입만 지원합니다."))
        }
    }
}

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class AccessToken