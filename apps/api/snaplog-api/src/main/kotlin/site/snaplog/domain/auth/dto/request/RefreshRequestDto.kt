package site.snaplog.domain.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "토큰 재발급 요청 DTO")
data class RefreshRequestDto(
    @field:Schema(description = "RefreshToken")
    @field:NotBlank(message = "refreshToken 값은 필수 값입니다.")
    val refreshToken: String
)
