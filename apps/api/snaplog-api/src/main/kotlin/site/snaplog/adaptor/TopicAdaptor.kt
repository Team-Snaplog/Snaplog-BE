package site.snaplog.adaptor

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.snaplog.domain.topic.dto.request.CreateTopicRequestDto
import site.snaplog.entity.TopicEntity
import site.snaplog.repository.TopicRepository

@Component
class TopicAdaptor(
    private val topicRepository: TopicRepository,
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
) {

    fun findAllByMemberId(memberId: String) = topicRepository.findAllByMemberId(memberId)
    fun findByTopicName(id: String, name: String) = topicRepository.findByMemberIdAndName(id, name)

    fun save(memberId: String, createTopicRequestDto: CreateTopicRequestDto): Mono<TopicEntity> {
        return Mono.just(
            TopicEntity(
                memberId = memberId,
                name = createTopicRequestDto.name,
                emoji = createTopicRequestDto.emoji
            )
        ).flatMap { r2dbcEntityTemplate.insert(it) }
    }

    fun delete(topicId: String) {
        topicRepository.deleteById(topicId)
    }
}