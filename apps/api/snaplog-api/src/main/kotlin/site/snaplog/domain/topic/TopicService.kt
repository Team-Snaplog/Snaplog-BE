package site.snaplog.domain.topic

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.snaplog.adaptor.TopicAdaptor
import site.snaplog.domain.topic.dto.request.CreateTopicRequestDto
import site.snaplog.entity.MemberEntity
import site.snaplog.entity.TopicEntity
import site.snaplog.enums.StatusCode
import site.snaplog.exception.SnaplogException

@Service
class TopicService(
    private val topicAdaptor: TopicAdaptor
) {

    fun getTopics(loginMember: MemberEntity): Flux<TopicEntity> {
        return topicAdaptor.findAllByMemberId(loginMember.id)
    }

    fun createTopic(loginMember: MemberEntity, createTopicRequestDto: CreateTopicRequestDto): Mono<TopicEntity> {
        return topicAdaptor.findByTopicName(loginMember.id, createTopicRequestDto.name)
            .flatMap { Mono.error<TopicEntity>(SnaplogException(statusCode = StatusCode.CONFLICT, "같은 이름의 주제가 이미 존재합니다.")) }
            .switchIfEmpty(topicAdaptor.save(loginMember.id, createTopicRequestDto))
    }

    fun deleteTopic(topicId: String) {
        topicAdaptor.delete(topicId)
    }
}