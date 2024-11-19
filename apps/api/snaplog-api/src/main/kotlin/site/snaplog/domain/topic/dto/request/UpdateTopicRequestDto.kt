package site.snaplog.domain.topic.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "주제 정보 수정 요청 DTO")
data class UpdateTopicRequestDto(
    @field:Schema(description = "주제 이름")
    val name: String?,

    @field:Schema(description = "주제 대표 이모티콘")
    val emoji: String?
)