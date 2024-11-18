package site.snaplog.domain.topic.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "주제 생성 요청 DTO")
data class CreateTopicRequestDto(
    @field:Schema(description = "주제 이름")
    @field:NotBlank(message = "name 값은 필수 값입니다.")
    val name: String,

    @field:Schema(description = "주제 대표 이모티콘")
    @field:NotBlank(message = "emoji 값은 필수 값입니다.")
    val emoji: String
)
