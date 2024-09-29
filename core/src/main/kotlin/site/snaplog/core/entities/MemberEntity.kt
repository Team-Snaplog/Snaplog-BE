package site.snaplog.core.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import site.snaplog.core.enums.Provider

@Table("member")
data class MemberEntity(
    @Id
    val id: Long? = null,
    val email: String,
    val provider: Provider
): BaseEntity()
