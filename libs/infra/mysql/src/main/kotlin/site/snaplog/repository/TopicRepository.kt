package site.snaplog.repository

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.snaplog.entity.TopicEntity

interface TopicRepository: R2dbcRepository<TopicEntity, Long> {

    fun findAllByMemberId(memberId: Long): Flux<TopicEntity>
    fun findByMemberIdAndName(id: Long, name: String): Mono<TopicEntity>
}