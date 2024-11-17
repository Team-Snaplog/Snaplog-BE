package site.snaplog.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("picture")
data class PictureEntity(
    @Id
    val id: String = UUID.randomUUID().toString().replace("-", ""),
    @Column("snap_id")
    val snapId: String,
    val url: String,
): BaseEntity()