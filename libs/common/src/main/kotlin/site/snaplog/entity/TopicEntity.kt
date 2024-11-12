package site.snaplog.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("subject")
data class TopicEntity(
    @Id
    val id: Long? = null,
    @Column("member_id")
    val memberId: Long,
    val name: String,
    val emoji: String,
    override val createdAt: LocalDateTime? = null,
    override val updatedAt: LocalDateTime? = null
): BaseEntity()