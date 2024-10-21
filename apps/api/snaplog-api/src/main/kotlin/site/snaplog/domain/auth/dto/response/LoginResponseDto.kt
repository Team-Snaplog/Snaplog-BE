package site.snaplog.domain.auth.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로그인 응답 DTO")
data class LoginResponseDto(
    @field:Schema(description = "AccessToken")
    val accessToken: String,

    @field:Schema(description = "RefreshToken")
    val refreshToken: String
)
