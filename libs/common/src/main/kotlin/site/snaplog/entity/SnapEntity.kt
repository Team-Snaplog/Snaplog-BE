package site.snaplog.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("snap")
data class SnapEntity(
    @Id
    val id: Long? = null,
    @Column("subject_id")
    val subjectId: Long,
    val content: String,
    @Column("snap_at")
    val snapAt: LocalDate,
): BaseEntity()
