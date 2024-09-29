package site.snaplog.config

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JasyptConfig {

    @Value("\${jasypt.encryptor.algorithm}")
    lateinit var algorithm: String

    @Value("\${jasypt.encryptor.pool-size}")
    var poolSize: Int = 0

    @Value("\${jasypt.encryptor.string-output-type}")
    lateinit var stringOutputType: String

    @Value("\${jasypt.encryptor.key-obtention-iterations}")
    var keyObtentionIterations: Int = 0

    @Value("\${jasypt.encryptor.password}")
    lateinit var password: String


    @Bean("jasyptStringEncryptor")
    fun jasyptEncryptor(): PooledPBEStringEncryptor {
        return PooledPBEStringEncryptor().apply {
            setAlgorithm(algorithm)
            setPoolSize(poolSize)
            setStringOutputType(stringOutputType)
            setKeyObtentionIterations(keyObtentionIterations)
            setPassword(password)
        }
    }
}