package site.snaplog.adaptor

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.snaplog.entity.MemberEntity
import site.snaplog.repository.MemberRepository

@Component
class MemberAdaptor(
    private val memberRepository: MemberRepository,
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
) {

    fun findMemberByEmail(email: String) = memberRepository.findByEmail(email)
    fun save(memberEntity: MemberEntity): Mono<MemberEntity> {
        return r2dbcEntityTemplate.insert(memberEntity)
    }
}