package site.snaplog.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("topic")
data class TopicEntity(
    @Id
    val id: String = UUID.randomUUID().toString().replace("-", ""),
    @Column("member_id")
    val memberId: String,
    val name: String,
    val emoji: String
): BaseEntity()