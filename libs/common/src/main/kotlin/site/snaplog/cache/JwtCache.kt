package site.snaplog.cache

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash("jwt")
data class JwtCache(
    @Id
    val email: String,
    val accessToken: String,
    @Indexed
    val refreshToken: String
)
