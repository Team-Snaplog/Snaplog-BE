package site.snaplog.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("picture")
data class PictureEntity(
    @Id
    val id: Long? = null,
    @Column("snap_id")
    val snapId: Long,
    val url: String,
    override val createdAt: LocalDateTime? = null,
    override val updatedAt: LocalDateTime? = null
): BaseEntity()