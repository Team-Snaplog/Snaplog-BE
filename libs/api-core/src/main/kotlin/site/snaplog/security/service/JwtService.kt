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
    private val jwtRepository: JwtCacheRepository
) {

    @Value("\${jwt.secret}")
    lateinit var secret: String

    fun issueTokens(memberEmail: String): JwtCache {
        val accessToken = createToken(memberEmail, JwtType.ACCESS)
        val refreshToken = createToken(memberEmail, JwtType.REFRESH)

        val jwt = JwtCache(email = memberEmail, accessToken = accessToken, refreshToken = refreshToken)
        return jwtRepository.save(jwt)
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
            try {
                val payload = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                    .build()
                    .parseSignedClaims(token)
                    .payload

                if (!payload.expiration.after(Date())) {
                    throw SnaplogException(StatusCode.UNAUTHORIZED, "만료된 JWT입니다.")
                }

                payload as Map<String, *>
            } catch (e: JwtException) {
                throw SnaplogException(StatusCode.UNAUTHORIZED, "유효하지 않은 JWT입니다.")
            }
        }
    }

    fun deleteTokenByEmail(memberEamil: String) {
        jwtRepository.deleteByEmail(memberEamil)
    }
}