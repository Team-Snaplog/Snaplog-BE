package site.snaplog.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import site.snaplog.cache.JwtBlacklistCache

interface JwtBlacklistCacheRepository: ReactiveCrudRepository<JwtBlacklistCache, String> {
    fun findByEmail(email: String): Mono<JwtBlacklistCache>

    fun deleteByEmail(email: String)
}