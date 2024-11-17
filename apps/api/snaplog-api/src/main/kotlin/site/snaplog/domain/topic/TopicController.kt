package site.snaplog.domain.topic

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.snaplog.domain.topic.dto.request.CreateTopicRequestDto
import site.snaplog.entity.MemberEntity
import site.snaplog.entity.TopicEntity
import site.snaplog.security.resolver.LoginMember
import site.snaplog.util.consts.Uri

@RestController
@RequestMapping(Uri.TOPICS)
@Tag(name = "Topic", description = "주제 관련 API")
class TopicController(
    private val topicService: TopicService
) {

    @GetMapping
    @Operation(
        summary = "회원 주제 목록 조회 Api",
        description = "회원별 주제 목록을 조회합니다."
    )
    fun getTopics(@LoginMember loginMember: MemberEntity): Flux<TopicEntity> {
        return topicService.getTopics(loginMember)
    }

    @PostMapping
    @Operation(
        summary = "주제 생성 Api",
        description = "주제를 생성합니다."
    )
    fun createTopic(@LoginMember loginMember: MemberEntity, @RequestBody @Valid createTopicRequestDto: CreateTopicRequestDto): Mono<TopicEntity> {
        return topicService.createTopic(loginMember, createTopicRequestDto)
    }
}