package site.snaplog.domain.topic

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import site.snaplog.adaptor.TopicAdaptor
import site.snaplog.entity.MemberEntity
import site.snaplog.entity.TopicEntity

@Service
class TopicService(
    private val topicAdaptor: TopicAdaptor
) {

    fun getTopics(loginMember: MemberEntity): Flux<TopicEntity> {
        return topicAdaptor.findAllByMemberId(loginMember.id!!)
    }
}