package site.snaplog.cache

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("jwt-blacklist")
data class BlacklistCache(
    @Id
    val email: String,

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    val expiration: Long
)