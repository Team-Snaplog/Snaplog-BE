package site.snaplog.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import site.snaplog.cache.JwtCache

interface JwtCacheRepository: ReactiveCrudRepository<JwtCache, String> {
    fun findByRefreshToken(refreshToken: String): Mono<JwtCache>

    fun deleteByEmail(email: String)
}