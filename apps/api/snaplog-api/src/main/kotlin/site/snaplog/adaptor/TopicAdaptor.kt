package site.snaplog.adaptor

import org.springframework.stereotype.Component
import site.snaplog.repository.TopicRepository

@Component
class TopicAdaptor(
    private val topicRepository: TopicRepository
) {

    fun findAllByMemberId(memberId: Long) = topicRepository.findAllByMemberId(memberId)
}