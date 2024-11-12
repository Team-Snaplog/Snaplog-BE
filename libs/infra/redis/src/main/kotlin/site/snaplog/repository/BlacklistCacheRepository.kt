package site.snaplog.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import site.snaplog.cache.BlacklistCache
import java.time.Duration

@Repository
class BlacklistCacheRepository(
    private val redisTemplate: ReactiveRedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    private val prefix = "blacklist:"

    fun save(blacklistCache: BlacklistCache, durationMillis: Long): Mono<BlacklistCache> {
        return redisTemplate.opsForValue()
            .set(
                "$prefix${blacklistCache.email}",
                objectMapper.writeValueAsString(blacklistCache),
                Duration.ofMillis(durationMillis)
            )
            .map { blacklistCache }
    }

    fun findByEmail(email: String): Mono<BlacklistCache> {
        return redisTemplate.opsForValue()
            .get("$prefix$email")
            .map { objectMapper.readValue(it, BlacklistCache::class.java) }
    }

    fun deleteByEmail(email: String): Mono<Boolean> {
        return redisTemplate.opsForValue()
            .delete("$prefix$email")
    }
}