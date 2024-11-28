package site.snaplog.domain.topic

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.snaplog.adaptor.TopicAdaptor
import site.snaplog.domain.topic.dto.request.CreateTopicRequestDto
import site.snaplog.domain.topic.dto.request.UpdateTopicRequestDto
import site.snaplog.entity.MemberEntity
import site.snaplog.entity.TopicEntity

@Service
class TopicService(
    private val topicAdaptor: TopicAdaptor
) {

    fun getTopics(loginMember: MemberEntity): Flux<TopicEntity> {
        return topicAdaptor.findAllByMemberId(loginMember.id)
    }

    fun createTopic(loginMember: MemberEntity, createTopicRequestDto: CreateTopicRequestDto): Mono<TopicEntity> {
        return topicAdaptor.save(loginMember.id, createTopicRequestDto)
    }

    fun deleteTopic(topicId: String): Mono<Void> {
        return topicAdaptor.delete(topicId)
    }

    fun updateTopic(topicId: String, updateTopicRequestDto: UpdateTopicRequestDto): Mono<TopicEntity> {
        return topicAdaptor.update(topicId, updateTopicRequestDto)
    }
}