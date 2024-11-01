package site.snaplog.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import site.snaplog.cache.JwtCache

@Repository
class JwtCacheRepository(
    private val redisTemplate: ReactiveRedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    private val prefix = "jwt:"

    fun save(jwtCache: JwtCache): Mono<JwtCache> {
        return redisTemplate.opsForValue()
            .set("$prefix${jwtCache.email}", objectMapper.writeValueAsString(jwtCache))
            .map { jwtCache }
    }

    fun findByEmail(email: String): Mono<JwtCache> {
        return redisTemplate.opsForValue()
            .get("$prefix$email")
            .map { objectMapper.readValue(it, JwtCache::class.java) }
    }

    fun deleteByEmail(email: String): Mono<Boolean> {
        return redisTemplate.opsForValue()
            .delete("$prefix$email")
    }
}