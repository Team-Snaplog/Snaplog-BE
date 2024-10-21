package site.snaplog.domain.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import site.snaplog.enums.Provider

@Schema(description = "로그인 요청 DTO")
data class LoginRequestDto(
    @field:Schema(description = "OAuth2 Provider")
    val provider: Provider,

    @field:Schema(description = "Google 및 Apple 로그인 시 발급받은 ID Token", example = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFiZDY3...")
    @field:NotBlank(message = "idToken 값은 필수 값입니다.")
    val idToken: String
)
