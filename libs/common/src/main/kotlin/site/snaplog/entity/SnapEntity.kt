package site.snaplog.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("snap")
data class SnapEntity(
    @Id
    val id: Long? = null,
    @Column("topic_id")
    val topicId: Long,
    val content: String,
    @Column("snap_at")
    val snapAt: LocalDate,
    override val createdAt: LocalDateTime? = null,
    override val updatedAt: LocalDateTime? = null
): BaseEntity()
