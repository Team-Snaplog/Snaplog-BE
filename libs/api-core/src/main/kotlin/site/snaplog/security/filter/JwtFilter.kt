package site.snaplog.security.filter

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException
import site.snaplog.repository.MemberRepository
import site.snaplog.security.service.JwtService
import site.snaplog.util.consts.Uri

@Component
class JwtFilter(
    private val jwtService: JwtService
): WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        if (isPassUri(exchange.request.uri.path)) {
            return chain.filter(exchange)
        }

        return Mono.justOrEmpty(
            exchange.request.headers.getFirst("Authorization")
                ?.substringAfter("Bearer ")
        )
            .switchIfEmpty(
                Mono.error(
                    SnaplogException(StatusCode.UNAUTHORIZED, "요청에 AccessToken이 존재하지 않습니다.")
                )
            )
            .flatMap { accessToken ->
                jwtService.getJwtPayload(accessToken)
            }
            .map { payload ->
                val email = payload["sub"] as String
                val authentication = UsernamePasswordAuthenticationToken(email, null, emptyList())
                ReactiveSecurityContextHolder.withAuthentication(authentication)
            }
            .flatMap { context ->
                chain.filter(exchange).contextWrite(context)
            }
    }

    fun isPassUri(uri: String): Boolean {
        return Uri.passUris.any { passUri ->
            if (passUri.endsWith("/**")) {
                uri.startsWith(passUri.removeSuffix("/**"))
            } else {
                uri == passUri
            }
        }
    }
}