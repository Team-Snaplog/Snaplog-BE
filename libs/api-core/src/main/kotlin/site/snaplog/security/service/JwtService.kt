package site.snaplog.security.service

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.snaplog.cache.JwtCache
import site.snaplog.enums.JwtType
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException
import site.snaplog.repository.JwtCacheRepository
import java.util.*

@Service
class JwtService(
    private val jwtCacheRepository: JwtCacheRepository
) {

    @Value("\${jwt.secret}")
    lateinit var secret: String

    fun issueTokens(memberEmail: String): Mono<Pair<String, String>> {
        val accessToken = createToken(memberEmail, JwtType.ACCESS)
        val refreshToken = createToken(memberEmail, JwtType.REFRESH)

        val jwt = JwtCache(email = memberEmail, refreshToken = refreshToken)
        return jwtCacheRepository.save(jwt)
            .map { accessToken to refreshToken }
    }

    fun createToken(memberEmail: String, jwtType: JwtType): String {
        val claims =Jwts.claims().subject(memberEmail).build()

        val issuedAt = Date()
        val expiration = when (jwtType) {
            JwtType.ACCESS -> Date(issuedAt.time + 1000 * 60 * 30)
            JwtType.REFRESH -> Date(issuedAt.time + 1000 * 60 * 60 * 24 * 14)
        }

        return Jwts.builder()
            .claims(claims)
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .compact()
    }

    fun getJwtPayload(token: String): Mono<Map<String, *>> {
        return Mono.fromCallable {
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .build()
                .parseSignedClaims(token)
                .payload
        }
            .onErrorMap { e ->
                when (e) {
                    is JwtException -> SnaplogException(StatusCode.UNAUTHORIZED, "유효하지 않은 JWT입니다.")
                    else -> e
                }
            }
            .flatMap { payload ->
                if (!payload.expiration.after(Date())) {
                    Mono.error(SnaplogException(StatusCode.UNAUTHORIZED, "만료된 JWT입니다."))
                } else {
                    Mono.just(payload as Map<String, *>)
                }
            }
    }

    fun findJwtCacheByEmail(email: String): Mono<JwtCache> {
        return jwtCacheRepository.findByEmail(email)
    }

    fun deleteJwtCache(email: String): Mono<Boolean> {
        return jwtCacheRepository.deleteByEmail(email)
    }
}