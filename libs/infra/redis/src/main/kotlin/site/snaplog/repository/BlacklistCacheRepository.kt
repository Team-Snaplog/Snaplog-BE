package site.snaplog.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import site.snaplog.cache.BlacklistCache

interface BlacklistCacheRepository: ReactiveCrudRepository<BlacklistCache, String> {
    fun findByEmail(email: String): Mono<BlacklistCache>

    fun deleteByEmail(email: String)
}