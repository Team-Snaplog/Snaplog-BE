package site.snaplog.repository

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono
import site.snaplog.entity.MemberEntity

interface MemberRepository: R2dbcRepository<MemberEntity, Long> {

    fun findByEmail(email: String): Mono<MemberEntity>
}