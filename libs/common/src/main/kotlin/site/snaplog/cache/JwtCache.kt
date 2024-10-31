package site.snaplog.cache

data class JwtCache(
    val email: String,
    val accessToken: String,
    val refreshToken: String
)
