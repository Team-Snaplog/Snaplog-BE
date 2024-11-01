package site.snaplog.security.resolver

import org.springframework.core.MethodParameter
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import site.snaplog.entity.MemberEntity
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException
import site.snaplog.repository.MemberRepository

@Component
class LoginMemberArgumentResolver(
    private val memberRepository: MemberRepository
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginMember::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any?> {
        return if (parameter.parameterType == MemberEntity::class.java) {
            ReactiveSecurityContextHolder.getContext()
                .map { it.authentication }
                .flatMap { authentication ->
                    when (val principal = authentication.principal) {
                        is MemberEntity -> Mono.just(principal)
                        else -> Mono.error(
                            SnaplogException(StatusCode.UNAUTHORIZED, "잘못된 인증 정보입니다.")
                        )
                    }
                }
        } else {
            Mono.error(SnaplogException(StatusCode.INTERNAL_SERVER_ERROR, "@LoginMember 어노테이션은 MemberEntity 타입만 지원합니다."))
        }
    }
}

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginMember