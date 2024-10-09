package site.snaplog.security.filter

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException
import site.snaplog.repository.MemberRepository
import site.snaplog.security.service.JwtService
import site.snaplog.util.consts.Uri

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
class JwtFilter(
    private val jwtService: JwtService,
    private val memberRepository: MemberRepository
): WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        if (isPassUri(exchange.request.uri.path)) {
            return chain.filter(exchange)
        }

        val accessToken = exchange.request.headers.getFirst("Authorization")
            ?.substringAfter("Bearer ")
            ?: throw SnaplogException(StatusCode.UNAUTHORIZED, "요청에 AccessToken이 존재하지 않습니다.")

        return jwtService.getJwtPayload(accessToken).map { payload ->
                memberRepository.findByEmail(payload["sub"].toString())
            }.switchIfEmpty { throw SnaplogException(StatusCode.UNAUTHORIZED, "JWT에 등록된 Email로 조회되는 회원이 존재하지 않습니다.") }
            .map { member ->
                val authentication = UsernamePasswordAuthenticationToken(member, null, emptyList())
                ReactiveSecurityContextHolder.withAuthentication(authentication)
            }.flatMap {
                chain.filter(exchange).contextWrite(it)
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