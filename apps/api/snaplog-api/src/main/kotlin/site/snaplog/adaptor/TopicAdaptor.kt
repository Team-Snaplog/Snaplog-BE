package site.snaplog.adaptor

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.snaplog.domain.topic.dto.request.CreateTopicRequestDto
import site.snaplog.entity.TopicEntity
import site.snaplog.repository.TopicRepository

@Component
class TopicAdaptor(
    private val topicRepository: TopicRepository
) {

    fun findAllByMemberId(memberId: Long) = topicRepository.findAllByMemberId(memberId)
    fun findByTopicName(id: Long, name: String) = topicRepository.findByMemberIdAndName(id, name)

    fun save(memberId: Long, createTopicRequestDto: CreateTopicRequestDto): Mono<TopicEntity> {
        return Mono.just(
            TopicEntity(
                memberId = memberId,
                name = createTopicRequestDto.name,
                emoji = createTopicRequestDto.emoji
            )
        ).flatMap { topicRepository.save(it) }
    }

}