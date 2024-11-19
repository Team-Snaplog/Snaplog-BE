package site.snaplog.adaptor

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.snaplog.domain.topic.dto.request.CreateTopicRequestDto
import site.snaplog.domain.topic.dto.request.UpdateTopicRequestDto
import site.snaplog.entity.TopicEntity
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException
import site.snaplog.repository.TopicRepository
import java.time.LocalDateTime

@Component
class TopicAdaptor(
    private val topicRepository: TopicRepository,
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
) {

    fun findAllByMemberId(memberId: String) = topicRepository.findAllByMemberId(memberId)

    fun save(memberId: String, createTopicRequestDto: CreateTopicRequestDto): Mono<TopicEntity> {
        return Mono.just(
            TopicEntity(
                memberId = memberId,
                name = createTopicRequestDto.name,
                emoji = createTopicRequestDto.emoji
            )
        ).flatMap { r2dbcEntityTemplate.insert(it) }
    }

    fun delete(topicId: String): Mono<Void> {
        return topicRepository.deleteById(topicId)
    }

    fun update(topicId: String, updateTopicRequestDto: UpdateTopicRequestDto): Mono<TopicEntity> {
        return topicRepository.findById(topicId)
            .switchIfEmpty(Mono.error(SnaplogException(statusCode = StatusCode.NOT_FOUND, message = "해당 Id로 저장된 주제가 없습니다.")))
            .map {
                it.copy(
                    name = updateTopicRequestDto.name ?: it.name,
                    emoji = updateTopicRequestDto.emoji ?: it.emoji
                )
            }
            .flatMap { topicRepository.save(it) }
    }
}