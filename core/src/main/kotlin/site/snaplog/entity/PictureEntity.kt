package site.snaplog.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("picture")
data class PictureEntity(
    @Id
    val id: Long? = null,
    @Column("subject_id")
    val subjectId: Long,
    val url: String
): BaseEntity()
