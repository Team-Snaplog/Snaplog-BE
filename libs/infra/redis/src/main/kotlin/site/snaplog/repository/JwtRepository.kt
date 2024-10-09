package site.snaplog.repository

import org.springframework.data.repository.CrudRepository
import site.snaplog.cache.JwtCache

interface JwtCacheRepository: CrudRepository<JwtCache, String> {
    fun findByRefreshToken(refreshToken: String): JwtCache?

    fun deleteByEmail(email: String)
}