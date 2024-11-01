package site.snaplog.adapter

import org.springframework.stereotype.Component
import site.snaplog.repository.MemberRepository

@Component
class MemberAdapter(
    private val memberRepository: MemberRepository
) {

    fun findMemberByEmail(email: String) = memberRepository.findByEmail(email)
}