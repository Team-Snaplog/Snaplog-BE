package site.snaplog.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("subject")
data class SubjectEntity(
    @Id
    val id: Long? = null,
    @Column("member_id")
    val memberId: Long,
    val name: String,
    val emoji: String
): BaseEntity()
