package site.snaplog.adaptor

import org.springframework.stereotype.Component
import site.snaplog.repository.MemberRepository

@Component
class MemberAdaptor(
    private val memberRepository: MemberRepository
) {

    fun findMemberByEmail(email: String) = memberRepository.findByEmail(email)
}