package site.snaplog.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import site.snaplog.enums.Provider

@Table("member")
data class MemberEntity(
    @Id
    val id: Long? = null,
    val email: String,
    val provider: Provider
): BaseEntity()
