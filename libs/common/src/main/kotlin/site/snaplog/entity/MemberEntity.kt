package site.snaplog.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import site.snaplog.enums.Provider
import java.util.UUID

@Table("member")
data class MemberEntity(
    @Id
    val id: String = UUID.randomUUID().toString().replace("-", ""),
    val email: String,
    val provider: Provider,
): BaseEntity()
