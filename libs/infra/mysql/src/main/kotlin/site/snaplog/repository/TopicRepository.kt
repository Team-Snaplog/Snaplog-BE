package site.snaplog.repository

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import site.snaplog.entity.TopicEntity

interface TopicRepository: R2dbcRepository<TopicEntity, String> {

    fun findAllByMemberId(memberId: String): Flux<TopicEntity>
}