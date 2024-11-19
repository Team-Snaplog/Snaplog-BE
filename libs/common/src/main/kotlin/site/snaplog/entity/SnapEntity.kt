package site.snaplog.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Table("snap")
data class SnapEntity(
    @Id
    val id: String = UUID.randomUUID().toString().replace("-", ""),

    @Column("topic_id")
    val topicId: String,

    val content: String,

    @Column("snap_at")
    val snapAt: LocalDate,

    @CreatedDate
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Column("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
