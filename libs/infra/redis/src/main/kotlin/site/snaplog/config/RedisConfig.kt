package site.snaplog.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Value("\${spring.data.redis.host}")
    lateinit var host: String

    @Value("\${spring.data.redis.port}")
    var port: Int = 0

    @Value("\${spring.data.redis.password}")
    lateinit var password: String

    @Value("\${spring.data.redis.database}")
    var database: Int = 0

    @Bean
    @Primary
    fun reactiveRedisconnectionFactory(): ReactiveRedisConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration(
            host,
            port
        )
        redisStandaloneConfiguration.database = database
        redisStandaloneConfiguration.password = RedisPassword.of(password)
        return LettuceConnectionFactory(redisStandaloneConfiguration)
    }

    @Bean
    fun reactiveRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, String> {
        val serializer = StringRedisSerializer()
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(String::class.java)
        val serializationContext = RedisSerializationContext.newSerializationContext<String, String>()
            .key(serializer)
            .value(jackson2JsonRedisSerializer)
            .hashKey(serializer)
            .hashValue(jackson2JsonRedisSerializer)
            .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }
}