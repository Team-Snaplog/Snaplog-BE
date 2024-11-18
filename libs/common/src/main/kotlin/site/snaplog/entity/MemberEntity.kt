package site.snaplog.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import site.snaplog.enums.Provider
import java.time.LocalDateTime
import java.util.UUID

@Table("member")
data class MemberEntity(
    @Id
    val id: String = UUID.randomUUID().toString().replace("-", ""),

    val email: String,

    val provider: Provider,

    @CreatedDate
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Column("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
