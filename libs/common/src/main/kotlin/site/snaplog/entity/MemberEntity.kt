package site.snaplog.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import site.snaplog.enums.Provider
import java.time.LocalDateTime

@Table("member")
data class MemberEntity(
    @Id
    val id: Long? = null,
    val email: String,
    val provider: Provider,
    override val createdAt: LocalDateTime? = null,
    override val updatedAt: LocalDateTime? = null
): BaseEntity()
